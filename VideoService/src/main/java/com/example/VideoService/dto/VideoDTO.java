package com.example.VideoService.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class VideoDTO {
    private String videoId;
    private String userId;
    private String bucketName;
    private String caption;
    private Instant uploadTime; // Do not set this in the constructor, will be set programmatically


    private List<String> tags;


    // Constructors
    public VideoDTO() {
    }

    public VideoDTO(String videoId, String userId, String bucketName,
                    String caption, Instant uploadTime, List<String> tags) {
        this.videoId = videoId;
        this.userId = userId;
        this.bucketName = bucketName;
        this.caption = caption;
        this.uploadTime = uploadTime;
        this.tags = tags;
    }
    public VideoDTO(String videoId, String userId, String bucketName, String caption) {
        this.videoId = videoId;
        this.userId = userId;
        this.bucketName = bucketName;
        this.caption = caption;
        this.uploadTime = Instant.now(); // Set the current time programmatically
    }
    // Getters and Setters
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
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

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


}
