package com.example.FollowService.commands;

public class UnFollowCommand implements command{
    private final String followerId;
    private final String followeeId;

    public UnFollowCommand(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    public String getFollowerId() {
        return followerId;
    }

    @Override
    public void execute() {
        return;
    }
}
