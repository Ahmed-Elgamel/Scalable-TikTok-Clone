package com.example.News.Feed.Service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsFeedService {




    public void addFeedItem(String userId, String content) {

    }

    public void deleteFeedItem(String userId, String content) {

    }

    @KafkaListener(topics = "video-upload-events", groupId = "newsfeed-group")
    public void handleVideoUploaded(String message) {
        System.out.println("📥 Received video uploaded event: " + message + " !!!!!!!!!!!!!!!!!!!!!!!!!");
        // parse JSON and update newsfeed logic here
    }



}
