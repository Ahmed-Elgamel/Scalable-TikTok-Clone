package com.example.News.Feed.Service.filter;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.FilterRequestDTO;
import com.example.News.Feed.Service.dto.VideoDTO;
import com.example.News.Feed.Service.service.FeedCacheService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByTagCommand implements NewsFeedFilterCommand{
    private final String userId;
    private final FeedCacheService cacheService;
    private final FilterRequestDTO filterRequestDTO;

    public FilterByTagCommand(String userId, FeedCacheService cacheService, FilterRequestDTO filterRequestDTO) {
        this.userId = userId;
        this.cacheService = cacheService;
        this.filterRequestDTO = filterRequestDTO;
    }
    @Override
    public FeedDTO execute() throws IOException {

        List<VideoDTO> userFeed = cacheService.getCachedFeedItems(userId);

        if (userFeed == null || userFeed.isEmpty()) {
            System.out.println("No feed items found for user: " + userId);
            return new FeedDTO(userId, new ArrayList<>());
        }
        List<String> tags = filterRequestDTO.getTags();
         userFeed = userFeed.stream()
                 .filter(video -> video.getTags().stream().anyMatch(tags::contains))
                 .collect(Collectors.toList());


        cacheService.cacheFeedItems(userId, userFeed);
        return new FeedDTO(userId, userFeed);
    }
}
