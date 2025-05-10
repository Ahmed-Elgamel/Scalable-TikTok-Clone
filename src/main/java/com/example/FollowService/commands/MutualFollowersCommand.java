package com.example.FollowService.commands;

public class MutualFollowersCommand implements command{
    private final String firstUserId;
    private final String secondUserId;

    public MutualFollowersCommand(String firstUserId, String secondUserId) {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    @Override
    public void execute() {
        return;
    }
}
