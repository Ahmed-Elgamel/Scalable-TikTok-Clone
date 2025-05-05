package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.VideoDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class FeedCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public FeedCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheFeedItems(String userId, List<VideoDTO> feedDTOs) {
        String key = userId;
        redisTemplate.opsForValue().set(key, feedDTOs);
        // Optional TTL
//        redisTemplate.expire(key, Duration.ofHours(1));
    }

    @SuppressWarnings("unchecked")
    public List<VideoDTO> getCachedFeedItems(String userId) {
        String key = userId;
        return (List<VideoDTO>) redisTemplate.opsForValue().get(key);
    }

    public void deleteFeedCache(String userId) {
        redisTemplate.delete(userId);
    }
}

