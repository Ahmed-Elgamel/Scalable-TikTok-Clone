package com.example.News.Feed.Service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "follow-service", url = "http://follow-ms-container:8080/api/follow")


public interface FollowServiceClient {
    @GetMapping("/followersIds/{followee}")
    List<String> getFollowers(@PathVariable("followee") String followee);
}