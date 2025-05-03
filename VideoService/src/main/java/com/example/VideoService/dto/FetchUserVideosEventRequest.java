package com.example.VideoService.dto;

import java.util.UUID;

public class FetchUserVideosEventRequest {

    private UUID requestId;     // Unique ID to match request and response
    private UUID userId;        // The user whose videos we want
    private int limit;          // How many videos to fetch
    private String replyTopic;  // Kafka topic for sending the response

    // Required no-arg constructor for deserialization (using jackson i think)
    public FetchUserVideosEventRequest() {
    }

    public FetchUserVideosEventRequest(UUID requestId, UUID userId, int limit, String replyTopic) {
        this.requestId = requestId;
        this.userId = userId;
        this.limit = limit;
        this.replyTopic = replyTopic;
    }

    // Getters and Setters
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
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

