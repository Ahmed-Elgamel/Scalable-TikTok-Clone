package com.example.FollowService.commands;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;

public class BlockCommand implements command{
    private final String followerId;
    private final String followeeId;
    private final FollowService followService;

    public BlockCommand(String followerId, String followeeId, FollowService followService) {
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

    public FollowService getFollowService() {
        return followService;
    }

    @Override
    public Follow execute() {
        return followService.handleBlockCommand(followerId,followeeId);

    }

}
