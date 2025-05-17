package com.example.News.Feed.Service.controller;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.FilterRequestDTO;
import com.example.News.Feed.Service.service.NewsFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/newsFeed")
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    public NewsFeedController(NewsFeedService newsFeedService){

        this.newsFeedService = newsFeedService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<FeedDTO> getNewsFeed(@PathVariable String userId) {
        FeedDTO feedDTO = newsFeedService.buildNewsFeed(userId);
        return ResponseEntity.ok(feedDTO);
    }


    @PostMapping("/filter/{userId}")
    public ResponseEntity<FeedDTO> filterNewsfeed(@PathVariable String userId,
                                                  @RequestBody FilterRequestDTO filterRequest) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.filterNewsFeed(userId, filterRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(feedDTO);
    }

    @GetMapping("/sort/{userId}")
    public ResponseEntity<FeedDTO>  sortNewsfeed(@PathVariable String userId,
                                         @RequestParam(defaultValue = "uploadTime") String strategy) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.sortNewsFeed(userId, strategy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(feedDTO);
    }







}
