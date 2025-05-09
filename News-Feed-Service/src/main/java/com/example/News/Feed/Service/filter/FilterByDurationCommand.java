package com.example.News.Feed.Service.filter;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.FilterRequestDTO;
import com.example.News.Feed.Service.dto.VideoDTO;
import com.example.News.Feed.Service.service.FeedCacheService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByDurationCommand implements NewsFeedFilterCommand{
    private final String userId;
    private final FeedCacheService cacheService;
    private final FilterRequestDTO filterRequestDTO;

    public FilterByDurationCommand(String userId, FeedCacheService cacheService, FilterRequestDTO filterRequestDTO) {
        this.userId = userId;
        this.cacheService = cacheService;
        this.filterRequestDTO = filterRequestDTO;
    }
    @Override
    public FeedDTO execute() throws IOException {
        // update the cached feed with the filtering criteria and also return the new newsfeed
        // our implementation assumes that there is some feed in the users cache

        List<VideoDTO> userFeed = cacheService.getCachedFeedItems(userId);

        if (userFeed == null || userFeed.isEmpty()) {
            System.out.println("No feed items found for user: " + userId);
            return new FeedDTO(userId, new ArrayList<>());
        }
        // filter these videos based on some command
        Double duration = filterRequestDTO.getDurationSeconds();
        userFeed = userFeed.stream()
                .filter(video -> video.getDurationSeconds() > duration) // Example: filter videos longer than 10 seconds
//                .filter(video -> video.getTags().contains("featured")) // Example: filter only featured videos
                .collect(Collectors.toList());


        // store back the updated feed in cache and return it
        cacheService.cacheFeedItems(userId, userFeed);
        return new FeedDTO(userId, userFeed);
    }
}
