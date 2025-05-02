package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.VideoDTO;
import com.example.News.Feed.Service.dto.VideoUploadEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsFeedService {

    /*
     here we build/feed the news feed for the user
     we could either use fan-out-on-read approach or fan-out-on-write approach
     if user is has not logged in for a long time or a new user then we use fan out on read
     if the user is an active user then we use fan-out-on-write

     in short:
     FAN-OUT-ON-READ --> 1) new users 2) empty/expired feeds
     FAN-OUT-ON-WRITE --> normal users
     */




    private final ObjectMapper objectMapper = new ObjectMapper();
    public void addFeedItem(String userId, String videoId) {

    }

    public void deleteFeedItem(String userId, String videoId) {

    }


    public static List<VideoDTO> buildNewsFeed(String userId){
        // see if there is something in cache
        // if not then get all the users this user follows (followees/followings)
        // then get all their uploaded videos
        // store in cache
        // return list of video DTOs
//        List<String> follwingsIds = new List<>(); // dummy list of followers
        // get all the uploads of this followings
        // update database + cache

        return null;
    }

    @KafkaListener(topics = "video-upload-events", groupId = "newsfeed-group") // use a FLINK NODE instead of this function?
    public void handleVideoUploaded(String message) throws JsonProcessingException {
        VideoUploadEvent event = objectMapper.readValue(message, VideoUploadEvent.class);
        System.out.println("Received video uploaded event: " + event.getVideoId() + " by " + event.getUserId());

        // get all the followers of this uploader
        // update news feed caches of all followers of this uploader
        // also update database?

    }



}
