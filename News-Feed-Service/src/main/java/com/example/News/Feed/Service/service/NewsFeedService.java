package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.dto.FetchUserVideosEventRequest;
import com.example.News.Feed.Service.dto.FeedDTO;
import com.example.News.Feed.Service.dto.FetchUserVideosEventResponse;
import com.example.News.Feed.Service.dto.VideoUploadEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    List<String> followeesIds = Arrays.asList(
            "b3c7f282-9f8d-4c37-a5b7-1d2d70ec6f52"
//            ,
//            "f91f9423-0f8e-4ea3-9bd7-c9d2c1c7639e",
//            "4cdbd23e-4425-4c1d-8f16-871b21a379ce",
//            "01f5c999-cc15-4f7a-bb9d-91e16c5eb9fb",
//            "abed0a2a-fc63-4ea0-8c76-c77e68fc4cb3",
//            "693a90ec-d7c2-49cc-8914-6cd205934c49",
//            "eef74026-03a0-4e5a-8d0d-78800e8cb5c2",
//            "908f9a15-62f7-4f92-8cb1-148b12e2d80f",
//            "3e17453c-bc38-41cf-878f-2f51134b3c02",
//            "53c2f5f3-5577-482b-a058-b56c0dc0ea9a"
    ); // fetched from follow service !!!!!!

    private final KafkaTemplate<String, FetchUserVideosEventRequest> kafkaTemplateRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsFeedService(KafkaTemplate<String, FetchUserVideosEventRequest> kafkaTemplateRequest) {
        this.kafkaTemplateRequest = kafkaTemplateRequest;
    }

    public void addFeedItem(String userId, String videoId) {

    }

    public void deleteFeedItem(String userId, String videoId) {

    }


    public List<FeedDTO> buildNewsFeed(String userId){
        // see if there is something in cache
        // if not then get all the users this user follows (followees/followings)
        // then get all their uploaded videos
        // store in cache
        // return list of video DTOs
        // get all the uploads of this followings
        // update database + cache

        // if not cached do this
        for (String followee: followeesIds){
            sendFetchUserVideosEvent(followee,"video.fetch.response");
        }

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

    public void sendFetchUserVideosEvent(String followee, String replyTopic){
        //send event to get videos of a followee
        FetchUserVideosEventRequest fetchUserVideosEventRequest = new FetchUserVideosEventRequest(UUID.randomUUID().toString(), followee, 10, replyTopic);
        kafkaTemplateRequest.send("video.fetch.request", fetchUserVideosEventRequest);
        System.out.println("****************************sending event to video service to fetch followee videos (NEWSFEED SERVICE)****************************");

    }

    @KafkaListener( topics = "video.fetch.response",
                    groupId = "newsfeed-videos-consumer-group",
                    containerFactory = "fetchUserVideosKafkaListenerFactory"
                    ) // use a FLINK NODE instead of this function?
    public void consumeFetchUserVideosEventResponse(FetchUserVideosEventResponse fetchUserVideosEventResponse){
        // update newsfeed cached and database!!
        System.out.println("****************************Received the focken videos of the followee****************************");

    }



}
