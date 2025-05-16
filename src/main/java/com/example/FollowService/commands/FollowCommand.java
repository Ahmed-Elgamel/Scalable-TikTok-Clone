package com.example.FollowService.commands;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;

public class FollowCommand implements command{
    private final String followerId;
    private final String followeeId;
    @Autowired
    private final FollowService followService;

    public FollowCommand(String followerId, String followeeId,FollowService followService) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followService=followService;
    }

    public String getFollowerId() {
        return followerId;
    }


    public String getFolloweeId() {
        return followeeId;
    }

    @Override
    public Follow execute() {
        return followService.handleFollowCommand(this.followerId,this.followeeId);
    }
}