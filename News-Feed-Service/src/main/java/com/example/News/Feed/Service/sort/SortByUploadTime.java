package com.example.News.Feed.Service.sort;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.VideoDTO;
import com.example.News.Feed.Service.service.FeedCacheService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SortByUploadTime implements NewsfeedSortStrategy{
    private final String userId;
    private final FeedCacheService cacheService;

    public SortByUploadTime(String userId, FeedCacheService cacheService) {
        this.userId = userId;
        this.cacheService = cacheService;
    }

    @Override
    public FeedDTO sort() throws IOException {
        List<VideoDTO> userFeed = cacheService.getCachedFeedItems(userId);

        // Sort the feed items by upload time, handling null values gracefully
        List<VideoDTO> sortedFeed = userFeed.stream()
                .sorted((v1, v2) -> {
                    if (v1.getUploadTime() == null && v2.getUploadTime() == null) return 0;
                    if (v1.getUploadTime() == null) return 1;  // nulls go to the bottom
                    if (v2.getUploadTime() == null) return -1;
                    return v1.getUploadTime().compareTo(v2.getUploadTime());
                })
                .collect(Collectors.toList());

        return new FeedDTO(userId, sortedFeed);
    }
}
