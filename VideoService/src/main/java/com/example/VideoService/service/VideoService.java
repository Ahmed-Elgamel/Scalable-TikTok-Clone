package com.example.VideoService.service;

import com.example.VideoService.VideoServiceApplication;
import com.example.VideoService.dto.FetchUserVideosEventRequest;
import com.example.VideoService.dto.FetchUserVideosEventResponse;
import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.repository.UserVideoRepository;
import com.example.VideoService.repository.VideoMetaDataRepository;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.example.VideoService.upload.strategy.UploadStrategy;
import com.example.VideoService.upload.strategy.UploadWithCaptionStrategy;
import com.example.VideoService.upload.strategy.UploadWithNoCaptionStrategy;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final S3Client s3Client;  // For S3
    private final MinioClient minioClient;  // For MinIO
    @Autowired
    private final VideoMetaDataRepository videoMetaDataRepository;
    @Autowired
    private final UserVideoRepository userVideoRepository;
    private final KafkaTemplate<String, String> kafkaVideoUploadTemplate;
    private final KafkaTemplate<String, FetchUserVideosEventResponse> kafkaUserVideosResponseTemplate;
    private static final String onVideoUploadTOPIC = "video-upload-events"; // Kafka topic to which we will send the event



    @Value("${minio.bucket}")
    private String bucketName;

    public VideoService(S3Client s3Client, MinioClient minioClient, VideoMetaDataRepository videoMetaDataRepository, UserVideoRepository userVideoRepository, KafkaTemplate<String, String> kafkaVideoUploadTemplate, KafkaTemplate<String, FetchUserVideosEventResponse> kafkaUserVideosResponseTemplate) {
        this.videoMetaDataRepository = videoMetaDataRepository;
        this.userVideoRepository = userVideoRepository;
        this.kafkaVideoUploadTemplate = kafkaVideoUploadTemplate;
        this.kafkaUserVideosResponseTemplate = kafkaUserVideosResponseTemplate;
        this.s3Client = null;
        this.minioClient = minioClient;
    }

    // Upload to S3 or MinIO
    public String uploadVideo(VideoDTO videoDTO, MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        String videoId = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filename = "videos/" + videoId ;

        videoDTO.setVideoId(videoId.substring(0, videoId.length() - 4)); // remove .mp4
        videoDTO.setBucketName(bucketName);


        if (s3Client != null) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        }

        if (minioClient != null) {
            minioClient.putObject(
                    io.minio.PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());


            String userId = videoDTO.getUserId();

            // Save metadata to DB depending on upload strategy provided from the user request
            UploadStrategy uploadStrategy;
            if(videoDTO.getCaption() == null)
                uploadStrategy = new UploadWithNoCaptionStrategy(videoMetaDataRepository, userVideoRepository);
            else
                uploadStrategy = new UploadWithCaptionStrategy(videoMetaDataRepository, userVideoRepository);

            uploadStrategy.saveVideoMetaData(videoDTO, file.getSize());
            uploadStrategy.saveUserVideo(videoDTO, file.getSize());

            //todo: send to user service?
            publishUploadEvent(videoId, userId); // send event to kafka to be later on be processed by consumers



        }


        return videoId;
    }

    public byte[] downloadVideo(String videoId) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] fileBytes = null;

        if (s3Client != null) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(videoId)
                    .build();

            fileBytes = s3Client.getObjectAsBytes(request).asByteArray();
        }

        if (minioClient != null) {
            fileBytes = minioClient.getObject(
                            io.minio.GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object("videos/" + videoId + ".mp4")
                                    .build())
                    .readAllBytes();
        }

        return fileBytes;
    }


    public void publishUploadEvent(String videoId, String userId){

        String videoUploadEvent = "{\"videoId\": \"" + videoId + "\", " +
                                        "\"userId\": \"" + userId + "\" }";
        System.out.println("****************************PUBLISHED VIDEO UPLOADED (VIDEO SERVICE)****************************");
        kafkaVideoUploadTemplate.send(onVideoUploadTOPIC, videoUploadEvent);
    }

    @KafkaListener(
            topics = "video.fetch.request",
            groupId = "newsfeed-consumer-group",
            containerFactory = "fetchUserVideosKafkaListenerFactory"

    )
    public void consumeFetchUserVideoEvent(FetchUserVideosEventRequest fetchUserVideosEventRequest){
        System.out.println("****************************(NEWS-FEED-CONSUMER-GROUP) Consumed event to fetch user's videoss (VIDEO SERVICE)****************************"+ fetchUserVideosEventRequest);
        UUID userId = fetchUserVideosEventRequest.getUserId();
        UUID targetUserId = fetchUserVideosEventRequest.getTargetUserId();

        List<VideoDTO> userVideos = userVideoRepository.findByKeyUserId(userId).stream()
                .map(userVideo -> new VideoDTO(
                        userVideo.getVideoId(),
                        userId.toString(),
                        userVideo.getBucketName(),
                        userVideo.getCaption(),
                        userVideo.getKey().getUploadTime()
                ))
                .collect(Collectors.toList());

        FetchUserVideosEventResponse fetchUserVideosEventResponse = new FetchUserVideosEventResponse(
                fetchUserVideosEventRequest.getRequestId(), //match request and response
                targetUserId,                               // user who wants these videos
                userId,                                     // userId
                userVideos                                  // uploaded videos of this userId
                );

        String replyTopic = fetchUserVideosEventRequest.getReplyTopic();

        kafkaUserVideosResponseTemplate.send(replyTopic, fetchUserVideosEventResponse);


    }



}
