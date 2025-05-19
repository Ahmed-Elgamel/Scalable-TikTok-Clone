package com.example.VideoService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "video_metadata")

public class VideoMetaData {

    @Id
    private String videoId; // same as in Video

    private double durationSeconds;
    private long sizeBytes;
    private String caption;

    private Instant uploadTime;

    private String bucketName;

    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }



    public VideoMetaData() {
    }

    public VideoMetaData(String videoId, double durationSeconds, long sizeBytes, String caption, Instant uploadTime, String bucketName) {
        this.videoId = videoId;
        this.durationSeconds = durationSeconds;
        this.sizeBytes = sizeBytes;
        this.caption = caption;
        this.uploadTime = uploadTime;
        this.bucketName = bucketName;
        this.tags = new ArrayList<>();
    }

    private VideoMetaData(Builder builder) {
        this.videoId = builder.videoId;
        this.caption = builder.caption;
        this.sizeBytes = builder.sizeBytes;
        this.durationSeconds = builder.durationSeconds;
        this.uploadTime = builder.uploadTime;
        this.bucketName = builder.bucketName;

        if(builder.tags != null)
            this.tags = builder.tags;
        else
            this.tags = new ArrayList<>();
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

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public static class Builder {
        private String videoId;
        private String caption=null;
        private long sizeBytes;
        private double durationSeconds;
        private Instant uploadTime;
        private String bucketName;
        private List<String> tags;

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder bucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

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

        public Builder durationSeconds(double durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }

        public Builder processedAt(Instant uploadTime) {
            this.uploadTime = uploadTime;
            return this;
        }

        public VideoMetaData build() {
            return new VideoMetaData(this);
        }
    }
}
