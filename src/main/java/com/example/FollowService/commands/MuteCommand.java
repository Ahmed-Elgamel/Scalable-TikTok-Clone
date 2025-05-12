package com.example.FollowService.commands;

import com.example.FollowService.Service.FollowService;

public class MuteCommand implements command {
    private final String followerId;
    private final String followeeId;
    private final FollowService followService;
    public MuteCommand(String followerId, String followeeId, FollowService followService) {
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
        followService.handleMuteCommand(followeeId,followerId);
      
    }
}
