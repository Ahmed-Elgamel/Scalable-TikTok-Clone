package com.example.News.Feed.Service.sort;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.VideoDTO;
import com.example.News.Feed.Service.service.FeedCacheService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SortByCaptionStrategy implements NewsfeedSortStrategy {

    private final String userId;
    private final FeedCacheService cacheService;

    public SortByCaptionStrategy(String userId, FeedCacheService cacheService) {
        this.userId = userId;
        this.cacheService = cacheService;
    }

    @Override
    public FeedDTO sort() throws IOException {
        List<VideoDTO> userFeed = cacheService.getCachedFeedItems(userId);

        // Sort the feed items by caption, handling null values gracefully
        List<VideoDTO> sortedFeed = userFeed.stream()
                .sorted((v1, v2) -> {
                    String caption1 = v1.getCaption();
                    String caption2 = v2.getCaption();

                    if (caption1 == null && caption2 == null) return 0;
                    if (caption1 == null) return 1;  // nulls go to the bottom
                    if (caption2 == null) return -1;
                    return caption1.compareToIgnoreCase(caption2);
                })
                .collect(Collectors.toList());

        return new FeedDTO(userId, sortedFeed);
    }
}