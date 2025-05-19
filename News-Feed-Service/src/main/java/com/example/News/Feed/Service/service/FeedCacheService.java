package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.VideoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


    public void deleteVideoFromCache(String videoId) throws IOException, JsonProcessingException {
        // Get all user keys (assuming they are stored as user IDs)
        Set<String> allUserKeys = redisTemplate.keys("*");

        if (allUserKeys == null || allUserKeys.isEmpty()) {
            return;
        }

        for (String userId : allUserKeys) {
            String json = (String) redisTemplate.opsForValue().get(userId);

            if (json == null) {
                continue;
            }

            List<VideoDTO> cachedFeed = objectMapper.readValue(json, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, VideoDTO.class));

            // Filter out the video
            List<VideoDTO> updatedFeed = cachedFeed.stream()
                    .filter(video -> !video.getVideoId().equals(videoId))
                    .collect(Collectors.toList());

            // Write back to Redis
            String updatedJson = objectMapper.writeValueAsString(updatedFeed);
            redisTemplate.opsForValue().set(userId, updatedJson);
        }



    }


    public void deleteAllVideosOfUser(String userId) throws IOException {
        // Get all keys from Redis
        Set<String> keys = redisTemplate.keys("*");

        if (keys == null || keys.isEmpty()) {
            System.out.println("No keys found in Redis.");
            return;
        }

        for (String key : keys) {
            String json = (String) redisTemplate.opsForValue().get(key);

            if (json == null) {
                continue;
            }

            List<VideoDTO> cachedFeed = objectMapper.readValue(json, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, VideoDTO.class));

            // Filter out all videos of the specified user
            List<VideoDTO> updatedFeed = cachedFeed.stream()
                    .filter(video -> !video.getUserId().equals(userId))
                    .collect(Collectors.toList());

            // Write back to Redis
            if (updatedFeed.isEmpty()) {
                redisTemplate.delete(key); // Remove the key if list is empty
            } else {
                String updatedJson = objectMapper.writeValueAsString(updatedFeed);
                redisTemplate.opsForValue().set(key, updatedJson);
            }
        }

        System.out.println("All videos of user " + userId + " have been removed from the cache.");
    }
}

