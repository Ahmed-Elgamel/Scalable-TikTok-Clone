package com.example.VideoService.controller;

import com.example.VideoService.service.VideoService;
import io.minio.errors.MinioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("nalbalbabalbalbalbalbalbalbalbalablablablabalblbalbalbalbalbalablbalblablabalb");
        String fileName = videoService.uploadVideo(file);
        return ResponseEntity.ok("Uploaded as: " + fileName);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] videoBytes = videoService.downloadVideo(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(videoBytes);
    }
}
