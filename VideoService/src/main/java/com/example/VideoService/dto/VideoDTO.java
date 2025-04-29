package com.example.VideoService.dto;

import java.time.LocalDateTime;

public class VideoDTO {
    private String videoId;
    private String userId;
    private String bucketName;
    private String caption;
    private LocalDateTime uploadTime;

    // Constructors
    public VideoDTO() {
    }

    public VideoDTO(String videoId, String userId, String bucketName,
                    String caption, LocalDateTime uploadTime) {
        this.videoId = videoId;
        this.userId = userId;
        this.bucketName = bucketName;
        this.caption = caption;
        this.uploadTime = uploadTime;
    }

    // Getters and Setters
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String id) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }


}
