package com.example.News.Feed.Service.controller;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.FilterRequestDTO;
import com.example.News.Feed.Service.model.FeedItem;
import com.example.News.Feed.Service.service.NewsFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/newsFeed")
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    public NewsFeedController(NewsFeedService newsFeedService) {

        this.newsFeedService = newsFeedService;
    }


//******************************************** CRUD *******************************************************
    // CREATE-UPDATE
    @PostMapping
    public FeedItem saveFeedItem(@RequestBody FeedItem feedItem){
        return newsFeedService.saveFeedItem(feedItem);
    }
    // READ
    @GetMapping("/{userId}/{uploadTime}")
    public Optional<FeedItem> getFeedItem(@PathVariable String userId, @PathVariable String uploadTime) {
        Instant uploadInstant = Instant.parse(uploadTime);
        return newsFeedService.getFeedItem(userId, uploadInstant);
    }
    //DELETE
    @DeleteMapping("/{userId}/{uploadTime}")
    public String deleteFeedItem(@PathVariable String userId, @PathVariable String uploadTime) {
        Instant uploadInstant = Instant.parse(uploadTime);
        newsFeedService.deleteFeedItem(userId, uploadInstant);
        return "FeedItem deleted successfully.";
    }
//******************************************** CRUD *******************************************************

    @GetMapping("/OldNewsFeed/{userId}")
    public ResponseEntity<FeedDTO> retreiveOldNewsFeedFromDb(@PathVariable String userId) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.retreiveOldNewsFeedFromDb(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(feedDTO);
    }

    @GetMapping("/refresh/{userId}")
    public ResponseEntity<FeedDTO> refreshNewsFeed(@PathVariable String userId) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.refreshNewsFeed(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(feedDTO);
    }

    @GetMapping("/build/{userId}")
    public ResponseEntity<FeedDTO> buildNewsFeed(@PathVariable String userId) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.buildNewsFeed(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public ResponseEntity<FeedDTO> sortNewsfeed(@PathVariable String userId,
                                                @RequestParam(defaultValue = "uploadTime") String strategy) {
        FeedDTO feedDTO = null;
        try {
            feedDTO = newsFeedService.sortNewsFeed(userId, strategy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(feedDTO);
    }

    @DeleteMapping("/video/{videoId}")
    public String deleteVideoFromCache(@PathVariable String videoId) {
        boolean deleted = newsFeedService.deleteVideo(videoId);
        if (deleted)
            return "Request to delete video " + videoId + " from cache is processed successfully.";
        else
            return "Request to delete video " + videoId + " from cache is processed not successfully.";
    }


    @DeleteMapping("/videos/user/{userId}")
    public String deleteAllVideosOfUserFromCache(@PathVariable String userId) {
        try {
            newsFeedService.deleteAllVideosOfUser(userId);
            return "All videos of user " + userId + " have been successfully deleted from the cache.";
        } catch (Exception e) {
            return "Failed to delete videos of user " + userId + " from the cache: " + e.getMessage();
        }
    }

}
