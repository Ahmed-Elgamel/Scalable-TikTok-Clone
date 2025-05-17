package com.example.News.Feed.Service.dto;

import java.util.UUID;

public class RequestFolloweesEvent {
    private UUID requestId;
    private String userId;
    private String replyTopic;

    public RequestFolloweesEvent() {}

    public RequestFolloweesEvent(UUID requestId, String userId, String replyTopic) {
        this.requestId = requestId;
        this.userId = userId;
        this.replyTopic = replyTopic;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyTopic() {
        return replyTopic;
    }

    public void setReplyTopic(String replyTopic) {
        this.replyTopic = replyTopic;
    }
}
