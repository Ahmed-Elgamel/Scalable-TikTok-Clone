package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.VideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FeedCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    public FeedCacheService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void cacheFeedItems(String userId, List<VideoDTO> feedDTOs) throws JsonProcessingException {
        String key = userId;
        String json = objectMapper.writeValueAsString(feedDTOs); // Manually serialize
        redisTemplate.opsForValue().set(key, json);
        // Optional TTL
//        redisTemplate.expire(key, Duration.ofHours(1));
    }

    public List<VideoDTO> getCachedFeedItems(String userId) throws IOException {
        String key = userId;
        String json = (String) redisTemplate.opsForValue().get(key); // Get from Redis

        if (json == null) {
            return null; // No cached data found
        }

        // Manually deserialize
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, VideoDTO.class));
    }


    public void deleteFeedCache(String userId) {
        redisTemplate.delete(userId);
    }
}

