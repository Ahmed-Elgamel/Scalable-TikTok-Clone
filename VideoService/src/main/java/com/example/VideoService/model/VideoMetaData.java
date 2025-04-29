package com.example.VideoService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "video_metadata")

public class VideoMetaData {

    @Id
    private String videoId; // same as in Video

    private double durationSeconds;
    private long sizeBytes;
    private String caption;

    private LocalDateTime processedAt;

    public VideoMetaData() {
    }

    public VideoMetaData(String videoId, double durationSeconds, long sizeBytes, String caption, LocalDateTime processedAt) {
        this.videoId = videoId;
        this.durationSeconds = durationSeconds;
        this.sizeBytes = sizeBytes;
        this.caption = caption;
        this.processedAt = processedAt;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
