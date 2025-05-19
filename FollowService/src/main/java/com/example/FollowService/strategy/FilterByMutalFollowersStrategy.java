package com.example.FollowService.strategy;

import com.example.FollowService.Model.Follow;
import com.example.FollowService.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByMutalFollowersStrategy implements Filter {
    private Integer numOfMutual;
    private String followeeId;
    private final FollowService followService;

    public FilterByMutalFollowersStrategy(String followeeId,Integer numOfMutual, FollowService followService) {
        this.followeeId = followeeId;
        this.numOfMutual = numOfMutual;
        this.followService = followService;
    }


    @Override
    public List<String> filter(List<Follow> followers) {
        return followers.stream()
                .map(Follow::getFollowerId)
                .distinct()
                .filter(followerId ->
                        followService.getMutualFollowers(followeeId, followerId)
                                .size() >= numOfMutual
                )
                .collect(Collectors.toList());

//        Map<String,Integer> mutualForEachFollower=followersIds.stream().map(follower-> follower,followService.getMutualFollowers(refrenceUser,follower).size());
    }

}
