package com.example.VideoService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Instant;

@Table("user_videos")
public class UserVideo {

    @PrimaryKeyClass
    public static class UserVideoKey {
        // partition on userId so a single node contains all the videos uploaded for this user
        @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        //sort on upload time so the most recent uploads are firstly fetched
        @PrimaryKeyColumn(name = "upload_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Instant uploadTime;

    }

    @PrimaryKey
    private UserVideoKey key;

    @Column("video_id")
    private UUID videoId; // same as in video

    @Column("duration_seconds")
    private double durationSeconds;
    @Column("size_bytes")
    private long sizeBytes;

    @Column("caption")
    private String caption;

    public UserVideoKey getKey() {
        return key;
    }

    public void setKey(UserVideoKey key) {
        this.key = key;
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
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
}

