package com.example.FollowService.commands;

import java.util.Date;

public class FilterByDateCommand implements command{
    private final String followeeId;
    private final Date date;

    public FilterByDateCommand(String followeeId, Date date) {
        this.followeeId = followeeId;
        this.date = date;
    }

    public String getFolloweeId() {
        return followeeId;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public void execute() {
        return;
    }
}
