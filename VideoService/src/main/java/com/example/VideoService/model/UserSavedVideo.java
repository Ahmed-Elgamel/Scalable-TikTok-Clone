package com.example.VideoService.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import java.util.UUID;
import java.time.Instant;

@Table("user_saved_videos")
public class UserSavedVideo {

    @PrimaryKeyClass
    public static class UserSavedVideoKey {
        @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        @PrimaryKeyColumn(name = "save_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Instant saveTime;

        public UserSavedVideoKey() {}

        public UserSavedVideoKey(UUID userId, Instant saveTime) {
            this.userId = userId;
            this.saveTime = saveTime;
        }
    }

    @PrimaryKey
    private UserSavedVideoKey key;

    @Column("saved_video_id")
    private String savedVideoId;

    public UserSavedVideoKey getKey() {
        return key;
    }

    public void setKey(UserSavedVideoKey key) {
        this.key = key;
    }

    public String getSavedVideoId() {
        return savedVideoId;
    }

    public void setSavedVideoId(String savedVideoId) {
        this.savedVideoId = savedVideoId;
    }
}
