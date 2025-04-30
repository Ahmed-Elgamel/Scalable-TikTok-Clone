package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.VideoUploadEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsFeedService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public void addFeedItem(String userId, String content) {

    }

    public void deleteFeedItem(String userId, String content) {

    }

    @KafkaListener(topics = "video-upload-events", groupId = "newsfeed-group")
    public void handleVideoUploaded(String message) throws JsonProcessingException {
        VideoUploadEvent event = objectMapper.readValue(message, VideoUploadEvent.class);
        System.out.println("Received video uploaded event: " + event.getVideoId() + " by " + event.getUserId());

    }



}
