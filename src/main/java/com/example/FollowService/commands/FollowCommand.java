package com.example.FollowService.commands;

public class FollowCommand implements command{
    private final String followerId;
    private final String followeeId;

    public FollowCommand(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public String getFollowerId() {
        return followerId;
    }


    public String getFolloweeId() {
        return followeeId;
    }

    @Override
    public String execute() {
        return "";
    }
}