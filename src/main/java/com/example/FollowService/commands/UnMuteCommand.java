package com.example.FollowService.commands;

import com.example.FollowService.Service.FollowService;

public class UnMuteCommand implements command{
    private final String followerId;
    private final String followeeId;
    private final FollowService followService;

    public UnMuteCommand(String followerId, String followeeId, FollowService followService) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followService = followService;
    }

    public String getFollowerId() {
        return followerId;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    @Override
    public void execute() {
        followService.handleUnMuteCommand(followeeId,followerId);

    }
}
