package com.example.VideoService.service;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class VideoService {

    private final S3Client s3Client;  // For S3
    private final MinioClient minioClient;  // For MinIO

    @Value("${minio.bucket}")
    private String bucketName;

    public VideoService(S3Client s3Client, MinioClient minioClient) {
        this.s3Client = null;
        this.minioClient = minioClient;
    }

    // Upload to S3 or MinIO
    public String uploadVideo(MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

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
        }

        return filename;
    }

    public byte[] downloadVideo(String filename) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] fileBytes = null;

        if (s3Client != null) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            fileBytes = s3Client.getObjectAsBytes(request).asByteArray();
        }

        if (minioClient != null) {
            fileBytes = minioClient.getObject(
                            io.minio.GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(filename)
                                    .build())
                    .readAllBytes();
        }

        return fileBytes;
    }
}
