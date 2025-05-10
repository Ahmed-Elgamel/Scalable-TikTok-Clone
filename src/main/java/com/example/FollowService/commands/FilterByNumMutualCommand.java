package com.example.FollowService.commands;

public class FilterByNumMutualCommand implements command{
    private final String followeeId;
    private final Integer numberOfMutual;

    public FilterByNumMutualCommand(String followeeId, Integer numberOfMutual) {
        this.followeeId = followeeId;
        this.numberOfMutual = numberOfMutual;
    }

    public Integer getNumberOfMutual() {
        return numberOfMutual;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    @Override
    public void execute() {
        return;
    }
}
