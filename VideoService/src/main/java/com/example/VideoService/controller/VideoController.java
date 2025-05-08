package com.example.VideoService.controller;

import com.example.VideoService.dto.VideoDTO;

import com.example.VideoService.seeder.DatabaseSeeder;
import com.example.VideoService.service.VideoService;
import io.minio.errors.MinioException;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private final DatabaseSeeder databaseSeeder;

    public VideoController(VideoService videoService, DatabaseSeeder databaseSeeder) {
        this.videoService = videoService;
        this.databaseSeeder = databaseSeeder;
    }



    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") VideoDTO metadata) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {

        if (metadata.getUploadTime() == null) {
            metadata.setUploadTime(Instant.now());
        }
        String videoId = videoService.uploadVideo(metadata, file);
        return ResponseEntity.ok("Uploaded Video with id: " + videoId);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] videoBytes = videoService.downloadVideo(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(videoBytes);
    }

    @DeleteMapping("/{userId}/{videoId}")
    public ResponseEntity<String> deleteVideoById(@PathVariable String userId, @PathVariable String videoId) {
        try {
            videoService.deleteVideoById(UUID.fromString(userId), videoId);  // Assuming your service has this method
            return ResponseEntity.ok("Video with ID: " + videoId + " was successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete video: " + e.getMessage());
        }
    }




    @PostMapping("/seed")
    public ResponseEntity<String> seed() {
        try {
            String response = databaseSeeder.seedVideos();  // If you want this to return something, you should change it
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Seeding failed: " + e.getMessage());
        }
    }



}
