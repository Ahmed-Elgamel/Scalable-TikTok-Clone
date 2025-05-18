package com.example.User.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "newsfeed-service", url = "http://newsfeed-ms-container:8080/newsFeed")


public interface NewsFeedServiceClient {
    @DeleteMapping("/videos/user/{userId}")
    String deleteAllUserVideosFromNewsFeed(@PathVariable("userId") String userId);
}
