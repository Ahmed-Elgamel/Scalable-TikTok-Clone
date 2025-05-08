package com.example.VideoService.service;

import com.example.VideoService.VideoServiceApplication;
import com.example.VideoService.dto.FetchUserVideosEventRequest;
import com.example.VideoService.dto.FetchUserVideosEventResponse;
import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.UserVideo;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.UserVideoRepository;
import com.example.VideoService.repository.VideoMetaDataRepository;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.example.VideoService.upload.strategy.UploadStrategy;
import com.example.VideoService.upload.strategy.UploadWithCaptionStrategy;
import com.example.VideoService.upload.strategy.UploadWithNoCaptionStrategy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

            uploadStrategy.saveVideoMetaData(videoDTO, file);
            uploadStrategy.saveUserVideo(videoDTO, file);

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
                        userVideo.getKey().getUploadTime(),
                        userVideo.getTags(),
                        userVideo.getDurationSeconds()

                ))
                .collect(Collectors.toList());



        FetchUserVideosEventResponse fetchUserVideosEventResponse = new FetchUserVideosEventResponse(
                fetchUserVideosEventRequest.getRequestId(), //match request and response
                targetUserId,                               // user who wants these videos
                userId,                                     // userId
                userVideos                                  // uploaded videos of this userId
                );

        for(VideoDTO videoDTO: fetchUserVideosEventResponse.getVideos()){
            System.out.println(videoDTO.getVideoId()+ " this is the video id bitchhhhhhhh");
        }

        String replyTopic = fetchUserVideosEventRequest.getReplyTopic();

        kafkaUserVideosResponseTemplate.send(replyTopic, fetchUserVideosEventResponse);


    }



    public String deleteVideoById(UUID userId, String videoId) {
        try {
            // Step 1: Find all videos for the user
            List<UserVideo> videos = userVideoRepository.findByKeyUserId(userId);

            // Step 2: Filter the video by videoId
            Optional<UserVideo> videoToDelete = videos.stream()
                    .filter(video -> video.getVideoId().equals(videoId))
                    .findFirst();

            if (videoToDelete.isEmpty()) {
                return "Video not found for userId: " + userId + " and videoId: " + videoId;
            }

            UserVideo video = videoToDelete.get();
            UserVideo.UserVideoKey key =video.getKey();

            // Step 3: Construct the filename for S3/MinIO
            String filename = "videos/" + videoId + ".mp4"; // assuming mp4 format

            // Step 4: Delete from S3
            if (s3Client != null) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filename)
                        .build();

                s3Client.deleteObject(deleteObjectRequest);
                System.out.println("Video successfully deleted from S3: " + filename);
            }

            // Step 5: Delete from MinIO
            if (minioClient != null) {
                minioClient.removeObject(
                        io.minio.RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(filename)
                                .build()
                );
                System.out.println("Video successfully deleted from MinIO: " + filename);
            }

            // Step 6: delete from metadata and user videos databases
            videoMetaDataRepository.deleteById(videoId);
            userVideoRepository.deleteByKeyUserIdAndKeyUploadTime(key.getUserId(), key.getUploadTime());

            return "Video with ID: " + videoId + " successfully deleted for userId: " + userId;
        } catch (Exception e) {
            System.err.println("Failed to delete video with ID: " + videoId + " for userId: " + userId + " - " + e.getMessage());
            return "Failed to delete video with ID: " + videoId + " for userId: " + userId + " - " + e.getMessage();
        }
    }

    public String updateVideoMetaData(UUID userId, String videoId, VideoDTO videoDTO) throws Exception {
        List<UserVideo> videos = userVideoRepository.findByKeyUserId(userId);

        UserVideo videoToUpdate = videos.stream()
                .filter(video -> video.getVideoId().equals(videoId))
                .findFirst().orElseThrow(
                        () -> new Exception("Video not found for userId: " + userId + " and videoId: " + videoId)
                );


        VideoMetaData videoMetaData = videoMetaDataRepository.findById(videoId).orElseThrow(
                () -> new Exception("Video MetaData not found")
        );
        // enumerate alll the fields found in videDTO and update uservideos table and video metadat table and save them again
        // use Reflection-based loop for updating fields
        Field[] fields = VideoDTO.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(videoDTO);

            // Skip if value is null
            if (value == null) {
                continue;
            }

            // Update UserVideo and VideoMetaData dynamically
            try {
                Field userVideoField = UserVideo.class.getDeclaredField(field.getName());
                userVideoField.setAccessible(true);
                userVideoField.set(videoToUpdate, value);

                Field videoMetaDataField = VideoMetaData.class.getDeclaredField(field.getName());
                videoMetaDataField.setAccessible(true);
                videoMetaDataField.set(videoMetaData, value);

            } catch (NoSuchFieldException e) {
                System.out.println("Field " + field.getName() + " not found in UserVideo or VideoMetaData. Skipping...");
            }
        }

        // Save the updated entities
        userVideoRepository.save(videoToUpdate);
        videoMetaDataRepository.save(videoMetaData);

        return "Video metadata successfully updated for videoId: " + videoId;
    }




}
