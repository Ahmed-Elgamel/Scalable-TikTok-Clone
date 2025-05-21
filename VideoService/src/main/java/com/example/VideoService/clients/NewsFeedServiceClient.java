package com.example.VideoService.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "newsfeed-service", url = "${NEWSFEED_SERVICE_URL}")

public interface NewsFeedServiceClient {
    @DeleteMapping("/video/{videoId}")
    String deleteVideoFromNewsFeed(@PathVariable("videoId") String videoId);
}
