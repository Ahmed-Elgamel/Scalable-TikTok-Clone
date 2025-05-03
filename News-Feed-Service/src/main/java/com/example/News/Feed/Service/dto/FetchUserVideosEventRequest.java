package com.example.News.Feed.Service.dto;

import java.util.UUID;

public class FetchUserVideosEventRequest {

    private String requestId;     // Unique ID to match request and response
    private String userId;        // The user whose videos we want
    private int limit;          // How many videos to fetch
    private String replyTopic;  // Kafka topic for sending the response

    // Required no-arg constructor for deserialization (using jackson i think)
    public FetchUserVideosEventRequest() {
    }

    public FetchUserVideosEventRequest(String requestId, String userId, int limit, String replyTopic) {
        this.requestId = requestId;
        this.userId = userId;
        this.limit = limit;
        this.replyTopic = replyTopic;
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getReplyTopic() {
        return replyTopic;
    }

    public void setReplyTopic(String replyTopic) {
        this.replyTopic = replyTopic;
    }
}

