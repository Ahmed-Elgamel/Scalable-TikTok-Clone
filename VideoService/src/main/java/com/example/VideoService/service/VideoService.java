package com.example.VideoService.service;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.repository.VideoMetaDataRepository;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.example.VideoService.strategy.UploadStrategy;
import com.example.VideoService.strategy.UploadWithCaptionStrategy;
import com.example.VideoService.strategy.UploadWithNoCaptionStrategy;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class VideoService {

    private final S3Client s3Client;  // For S3
    private final MinioClient minioClient;  // For MinIO
    @Autowired
    private final VideoMetaDataRepository videoMetaDataRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    public VideoService(S3Client s3Client, MinioClient minioClient, VideoMetaDataRepository videoMetaDataRepository) {
        this.videoMetaDataRepository = videoMetaDataRepository;
        this.s3Client = null;
        this.minioClient = minioClient;
    }

    // Upload to S3 or MinIO
    public String uploadVideo(VideoDTO videoDTO, MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        String videoId = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filename = "videos/" + videoId ;


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


            String userId = videoDTO.getUserId(); //todo: send to user service?

            // Save metadata to DB depending on upload strategy provided from the user request
            UploadStrategy uploadStrategy;
            if(videoDTO.getCaption() == null)
                uploadStrategy = new UploadWithNoCaptionStrategy(videoMetaDataRepository);
            else
                uploadStrategy = new UploadWithCaptionStrategy(videoMetaDataRepository);

            uploadStrategy.saveVideoMetaData(videoDTO,file.getSize());


//            VideoMetaData videoMetaData = new VideoMetaData();
//            videoMetaData.setSizeBytes(file.getSize());
//            videoMetaData.setVideoId(videoId);
//            videoMetaData.setDurationSeconds(0);  //todo  note: this needs an external library like FFMPEG for example
//            videoMetaData.setSizeBytes(file.getSize());
//            videoMetaData.setCaption(videoDTO.getCaption());
//            videoMetaData.setProcessedAt(LocalDateTime.now());
//            videoMetaDataRepository.save(videoMetaData);
//


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
}
