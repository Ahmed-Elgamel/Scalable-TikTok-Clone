package com.example.VideoService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "video_metadata")

public class VideoMetaData {

    @Id
    private String videoId; // same as in Video

    private double durationSeconds;
    private long sizeBytes;
    private String caption;

    private Instant processedAt;

    public VideoMetaData() {
    }

    public VideoMetaData(String videoId, double durationSeconds, long sizeBytes, String caption, Instant processedAt) {
        this.videoId = videoId;
        this.durationSeconds = durationSeconds;
        this.sizeBytes = sizeBytes;
        this.caption = caption;
        this.processedAt = processedAt;
    }

    private VideoMetaData(Builder builder) {
        this.videoId = builder.videoId;
        this.caption = builder.caption;
        this.sizeBytes = builder.sizeBytes;
        this.durationSeconds = builder.durationSeconds;
        this.processedAt = builder.processedAt;
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

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public static class Builder {
        private String videoId;
        private String caption=null;
        private long sizeBytes;
        private int durationSeconds;
        private Instant processedAt;

        public Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder sizeBytes(long sizeBytes) {
            this.sizeBytes = sizeBytes;
            return this;
        }

        public Builder durationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }

        public Builder processedAt(Instant processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public VideoMetaData build() {
            return new VideoMetaData(this);
        }
    }
}
