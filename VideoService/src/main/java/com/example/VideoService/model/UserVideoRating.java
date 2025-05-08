package com.example.VideoService.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import java.util.UUID;

@Table("user_video_ratings")
public class UserVideoRating {

    @PrimaryKeyClass
    public static class UserVideoRatingKey {
        @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        @PrimaryKeyColumn(name = "video_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private String videoId;

        public UserVideoRatingKey() {}

        public UserVideoRatingKey(UUID userId, String videoId) {
            this.userId = userId;
            this.videoId = videoId;
        }
    }

    @PrimaryKey
    private UserVideoRatingKey key;

    @Column("rating")
    private int rating;

    // Getters and Setters
}
