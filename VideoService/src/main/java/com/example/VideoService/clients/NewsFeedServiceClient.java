package com.example.VideoService.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "newsfeed-service", url = "http://newsfeed-ms-container:8080/newsFeed")


public interface NewsFeedServiceClient {
    @DeleteMapping("/video/{videoId}")
    String deleteVideoFromNewsFeed(@PathVariable("videoId") String videoId);
}
