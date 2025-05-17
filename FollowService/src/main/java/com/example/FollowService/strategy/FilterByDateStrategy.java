package com.example.FollowService.strategy;

import com.example.FollowService.Model.Follow;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByDateStrategy implements Filter {
    private Date startDate;

    public FilterByDateStrategy(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public List<String> filter(List<Follow> followers) {
        return followers.stream()
                .filter(follow -> follow.getFollowedAt().after(startDate))
                .map(Follow::getFollowerId)
                .collect(Collectors.toList());
    }
}

