package com.example.User.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "newsfeed-service", url = "${NEWSFEED_SERVICE_URL}")


public interface NewsFeedServiceClient {
    @DeleteMapping("/videos/user/{userId}")
    String deleteAllUserVideosFromNewsFeed(@PathVariable("userId") String userId);
}
