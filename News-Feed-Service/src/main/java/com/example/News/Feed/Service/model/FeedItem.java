package com.example.News.Feed.Service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feed_item")

public class FeedItem {
    @Id
    private String feedItemId;
    private String userId;     // The receiver of the post
    private String videoId;

    public FeedItem(){

    }

    public FeedItem(String userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    public String getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(String feedItemId) {
        this.feedItemId = feedItemId;
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
