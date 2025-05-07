package com.example.News.Feed.Service.model;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.List;

@Table("feed_items")
public class FeedItem {

    @PrimaryKeyClass
    public static class FeedItemKey {

        @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private String userId;

        @PrimaryKeyColumn(name = "upload_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Instant uploadTime;

        public FeedItemKey() {}

        public FeedItemKey(String userId, Instant uploadTime) {
            this.userId = userId;
            this.uploadTime = uploadTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Instant getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(Instant uploadTime) {
            this.uploadTime = uploadTime;
        }
    }

    @PrimaryKey
    private FeedItemKey key;

    @Column("video_id")
    private String videoId;

    @Column("bucket_name")
    private String bucketName;

    @Column("caption")
    private String caption;

    @Column("tags")
    private List<String> tags;


    public FeedItem() {}


    public FeedItem(FeedItemKey key, String videoId, String bucketName, String caption, List<String> tags) {
        this.key = key;
        this.videoId = videoId;
        this.bucketName = bucketName;
        this.caption = caption;
        this.tags = tags;
    }

    public FeedItemKey getKey() {
        return key;
    }

    public void setKey(FeedItemKey key) {
        this.key = key;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
        return key != null ? key.getUploadTime() : null;
    }

    public String getUserId() {
        return key != null ? key.getUserId() : null;
    }
}
