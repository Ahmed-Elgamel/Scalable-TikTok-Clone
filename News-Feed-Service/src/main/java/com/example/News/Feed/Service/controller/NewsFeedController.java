package com.example.News.Feed.Service.controller;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.service.NewsFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/newsFeed")
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    public NewsFeedController(NewsFeedService newsFeedService){

        this.newsFeedService = newsFeedService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<FeedDTO>> getNewsFeed(@PathVariable String userId) {
        List<FeedDTO> feedDTOS = newsFeedService.buildNewsFeed(userId);
        return ResponseEntity.ok(feedDTOS);
    }



}
