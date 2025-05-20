package com.example.News.Feed.Service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "follow-service", url = "${FOLLOW_SERVICE_URL}")

public interface FollowServiceClient {
    @GetMapping("/followersIds/{followee}")
    List<String> getFollowers(@PathVariable("followee") String followee);
}