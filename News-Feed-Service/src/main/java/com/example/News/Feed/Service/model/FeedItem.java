package com.example.News.Feed.Service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feed_item")

public class FeedItem {
    @Id
    private String userId;
    private String videoId;

    public FeedItem(){

    }

    public FeedItem(String userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
