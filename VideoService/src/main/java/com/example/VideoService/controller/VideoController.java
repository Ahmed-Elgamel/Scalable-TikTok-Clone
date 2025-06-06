package com.example.VideoService.controller;

import com.example.VideoService.dto.VideoDTO;

import com.example.VideoService.model.UserSavedVideo;
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
import java.util.List;
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

    @PatchMapping("/{userId}/{videoId}")
    public ResponseEntity<String> updateVideoMetaData(@PathVariable String userId,
                                                      @PathVariable String videoId,
                                                      @RequestBody VideoDTO videoDTO) {
        try {
            videoService.updateVideoMetaData(UUID.fromString(userId), videoId, videoDTO);  // Assuming this method exists
            return ResponseEntity.ok("Video metadata for ID: " + videoId + " was successfully updated.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update video metadata: " + e.getMessage());
        }
    }

    @PostMapping("/save/{userId}/{videoId}")
    public ResponseEntity<String> saveVideoForUser(@PathVariable String userId, @PathVariable String videoId) {
        try {
            videoService.saveVideoForUser(UUID.fromString(userId), videoId);
            return ResponseEntity.ok("Video with ID: " + videoId + " was successfully saved for user: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save video for user: " + e.getMessage());
        }
    }

    @GetMapping("/save/{userId}")
    public ResponseEntity<?> getUserSavedVideos(@PathVariable String userId) {
        try {
            List<UserSavedVideo> userSavedVideos = videoService.getUserSavedVideos(UUID.fromString(userId));
            return ResponseEntity.ok(userSavedVideos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to get saved videos for user: " + e.getMessage());
        }
    }

    @PostMapping("/rate/{userId}/{videoId}")
    public ResponseEntity<String> rateVideo(
            @PathVariable String userId,
            @PathVariable String videoId,
            @RequestParam int rating) {
        try {
            videoService.rateVideo(UUID.fromString(userId), videoId, rating);
            return ResponseEntity.ok("Video with ID: " + videoId + " was successfully rated as " + rating + " by user: " + userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to rate video: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/{videoId}/tags")
    public ResponseEntity<VideoDTO> addTags(
            @PathVariable UUID userId,
            @PathVariable String videoId,
            @RequestBody List<String> tags) {

        try {
            VideoDTO updatedVideoDTO = videoService.addTags(userId, videoId, tags);
            return ResponseEntity.ok(updatedVideoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
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
