package com.example.FollowService.strategy;

import com.example.FollowService.Model.Follow;

import java.util.List;

public interface Filter {
    List<String> filter(List<Follow> followers);
}
