package com.example.FollowService.commands;

import com.example.FollowService.Service.FollowService;

public class UnFollowCommand implements command{
    private final String followerId;
    private final String followeeId;
    private final FollowService followService;

    public UnFollowCommand(String followerId, String followeeId, FollowService followService) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followService = followService;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    public String getFollowerId() {
        return followerId;
    }

    @Override
    public void execute() {

        followService.handleUnFollowCommand(this.followerId,this.followeeId);
    }
}
