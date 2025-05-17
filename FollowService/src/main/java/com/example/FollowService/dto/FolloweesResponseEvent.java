package com.example.FollowService.dto;

import java.util.List;

public class FolloweesResponseEvent {
    private String userId;
    private List<String> followeeIds;

    public FolloweesResponseEvent() {}

    public FolloweesResponseEvent(String userId, List<String> followeeIds) {
        this.userId = userId;
        this.followeeIds = followeeIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFolloweeIds() {
        return followeeIds;
    }

    public void setFolloweeIds(List<String> followeeIds) {
        this.followeeIds = followeeIds;
    }
}
