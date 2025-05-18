package com.example.News.Feed.Service.service;

import com.example.News.Feed.Service.clients.FollowServiceClient;
import com.example.News.Feed.Service.dto.*;
import com.example.News.Feed.Service.filter.FilterByDurationCommand;
import com.example.News.Feed.Service.filter.FilterByTagCommand;
import com.example.News.Feed.Service.filter.NewsFeedFilterCommand;
import com.example.News.Feed.Service.model.FeedItem;
import com.example.News.Feed.Service.repository.FeedItemRepository;
import com.example.News.Feed.Service.sort.NewsfeedSortStrategy;
import com.example.News.Feed.Service.sort.SortByCaptionStrategy;
import com.example.News.Feed.Service.sort.SortByUploadTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
           "e4eaaaf2-d142-11e1-b3e4-080027620cdd",
            "f91f9423-0f8e-4ea3-9bd7-c9d2c1c7639e",
            "4cdbd23e-4425-4c1d-8f16-871b21a379ce",
            "01f5c999-cc15-4f7a-bb9d-91e16c5eb9fb",
            "abed0a2a-fc63-4ea0-8c76-c77e68fc4cb3",
            "693a90ec-d7c2-49cc-8914-6cd205934c49",
            "eef74026-03a0-4e5a-8d0d-78800e8cb5c2",
            "908f9a15-62f7-4f92-8cb1-148b12e2d80f",
            "3e17453c-bc38-41cf-878f-2f51134b3c02",
            "53c2f5f3-5577-482b-a058-b56c0dc0ea9a"
    ); // fetched from follow service !!!!!!

    private final KafkaTemplate<String, FetchUserVideosEventRequest> kafkaTemplateRequest;
    private final KafkaTemplate<String, RequestFolloweesEvent> kafkaFetchUserFolloweesRequest;

    private final FeedItemRepository feedItemRepository;
    @Autowired
    private final FeedCacheService feedCacheService;
    private final FollowServiceClient followServiceClient;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsFeedService(KafkaTemplate<String, FetchUserVideosEventRequest> kafkaTemplateRequest, KafkaTemplate<String, RequestFolloweesEvent> kafkaFetchUserFolloweesRequest, FeedItemRepository feedItemRepository, FeedCacheService feedCacheService, FollowServiceClient followServiceClient) {
        this.kafkaTemplateRequest = kafkaTemplateRequest;
        this.kafkaFetchUserFolloweesRequest = kafkaFetchUserFolloweesRequest;
        this.feedItemRepository = feedItemRepository;
        this.feedCacheService = feedCacheService;
        this.followServiceClient = followServiceClient;
    }

    // create-update
    public FeedItem saveFeedItem(FeedItem feedItem) {
        return feedItemRepository.save(feedItem);
    }
    // read
    public Optional<FeedItem> getFeedItem(String userId, Instant uploadTime) {
        FeedItem.FeedItemKey key = new FeedItem.FeedItemKey(userId, uploadTime);
        return feedItemRepository.findById(key);
    }
    // delete
    public void deleteFeedItem(String userId, Instant uploadTime) {
        FeedItem.FeedItemKey key = new FeedItem.FeedItemKey(userId, uploadTime);
        feedItemRepository.deleteById(key);
    }


    public FeedDTO buildNewsFeed(String userId) throws IOException {
        // see if there is something in cache if yes return it
        //*********************************************************************************
        // if not then get all the users this user follows (followees/followings)
        // then get all their uploaded videos
        // store in cache
        // return list of video DTOs
        // get all the uploads of this followings
        // update database + cache

        List<VideoDTO> cachedFeed = feedCacheService.getCachedFeedItems(userId);
        if(cachedFeed !=null && cachedFeed.size()>0){
            return new FeedDTO(userId, cachedFeed);
        }

        // if not cached do this
        sendFetchUserFollowees(userId, "followees.fetch.response");
//        for (String followee: followeesIds){
//            // fire and forget (non-blocking operation because we dont wait for the response)
//            sendFetchUserVideosEvent(userId ,followee,"video.fetch.response");
//        }

        return null;
    }


    public FeedDTO filterNewsFeed(String userId, FilterRequestDTO filterRequestDTO) throws IOException {
        NewsFeedFilterCommand newsFeedFilterCommand;
        FeedDTO feedDTO = null;

        if(filterRequestDTO.getDurationSeconds() != null){
            newsFeedFilterCommand = new FilterByDurationCommand(userId, feedCacheService, filterRequestDTO);
            feedDTO = newsFeedFilterCommand.execute();

        }
        if(filterRequestDTO.getTags() != null){
            newsFeedFilterCommand = new FilterByTagCommand(userId, feedCacheService, filterRequestDTO);
            feedDTO = newsFeedFilterCommand.execute();

        }
        return feedDTO;
    }

    public FeedDTO sortNewsFeed(String userId, String strategy) throws IOException {
       FeedDTO feedDTO = null;
        NewsfeedSortStrategy newsfeedSortStrategy;

        if(strategy.equals("uploadTime")){
            newsfeedSortStrategy = new SortByUploadTime(userId, feedCacheService);
            feedDTO = newsfeedSortStrategy.sort();
        }
        else if(strategy.equals("caption")){
            newsfeedSortStrategy = new SortByCaptionStrategy(userId, feedCacheService);
            feedDTO = newsfeedSortStrategy.sort();
       }

       return feedDTO;
    }

    public boolean deleteVideo(String videoId){
        try {
            feedCacheService.deleteVideoFromCache(videoId);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean deleteAllVideosOfUser(String userId){
        try {
            feedCacheService.deleteAllVideosOfUser(userId);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public void sendFetchUserFollowees(String userId, String replyTopic){
        //send event to get videos of a followee
        RequestFolloweesEvent event = new RequestFolloweesEvent(
                UUID.randomUUID(),
                userId,
                replyTopic
        );
        kafkaFetchUserFolloweesRequest.send("followees.fetch.request", event);
        System.out.println("****************************sending event to follow service to fetch user followees (NEWSFEED SERVICE)****************************");

    }

    @KafkaListener( topics = "followees.fetch.response",
            groupId = "newsfeed-followees-consumer-group",
            containerFactory = "fetchUserFolloweesKafkaListenerFactory"
    )
    public void consumeUserFollowees(FolloweesResponseEvent followeesResponseEvent)  {
        // now we received the followees of the target user
        // now for all of those followers send event to fetch their videos
        System.out.println("****************************Received followees of user (NEWSFEED SERVICE)****************************");

        String userId = followeesResponseEvent.getUserId();

        for (String followee: followeesResponseEvent.getFolloweeIds()){
            // fire and forget (non-blocking operation because we dont wait for the response)
            sendFetchUserVideosEvent(userId ,followee,"video.fetch.response");
        }



    }


    @KafkaListener(topics = "video-upload-events", groupId = "newsfeed-group") // use a FLINK NODE instead of this function?
    public void handleVideoUploaded(String message) throws JsonProcessingException {
        VideoUploadEvent event = objectMapper.readValue(message, VideoUploadEvent.class);
        System.out.println("Received video uploaded event: " + event.getVideoId() + " by " + event.getUserId());

        // get all the followers of this uploader via feign client //todo: make it async in future
        List<String> followers = followServiceClient.getFollowers(event.getUserId());
        // update news feed caches of all followers of this uploader
        // also update database?


    }

    public void sendFetchUserVideosEvent(String targetUserId,String followee, String replyTopic){
        //send event to get videos of a followee
        FetchUserVideosEventRequest fetchUserVideosEventRequest = new FetchUserVideosEventRequest(
                UUID.randomUUID(),
                UUID.fromString(targetUserId),
                UUID.fromString(followee),
                10,
                replyTopic);
        kafkaTemplateRequest.send("video.fetch.request", fetchUserVideosEventRequest);
        System.out.println("****************************sending event to video service to fetch followee videos (NEWSFEED SERVICE)****************************");

    }

    @KafkaListener( topics = "video.fetch.response",
                    groupId = "newsfeed-videos-consumer-group",
                    containerFactory = "fetchUserVideosKafkaListenerFactory"
                    ) // use a FLINK NODE instead of this function?
    public void consumeFetchUserVideosEventResponse(FetchUserVideosEventResponse fetchUserVideosEventResponse) throws IOException {
        // update newsfeed cache and database!!
        System.out.println("****************************Received the focken videos of the followee****************************");
        List<FeedItem> feedItems = fetchUserVideosEventResponse.getVideos().stream().
                map(userVideoDTO ->
                        new FeedItem(
                                new FeedItem.FeedItemKey(userVideoDTO.getUserId(), userVideoDTO.getUploadTime()),
                                userVideoDTO.getVideoId(),
                                userVideoDTO.getBucketName(),
                                userVideoDTO.getCaption(),
                                userVideoDTO.getTags(),
                                userVideoDTO.getDurationSeconds()
                                    )).
                collect(Collectors.toList());

        for(VideoDTO videoDTO: fetchUserVideosEventResponse.getVideos()){
            System.out.println(videoDTO.getVideoId()+ " this is the video id bitchhhhhhhh");
        }

        // save to db and cache
        feedItemRepository.saveAll(feedItems); //save to db

        String targetUserId = fetchUserVideosEventResponse.getTargetUserId().toString();
        List<VideoDTO> cachedFeedOfTargetUser = feedCacheService.getCachedFeedItems(targetUserId);
        List<VideoDTO> updatedFeedOfTargetUser = new ArrayList<>();

        if(cachedFeedOfTargetUser != null)
            updatedFeedOfTargetUser.addAll(cachedFeedOfTargetUser); // add the already cached feed to the list


        List<VideoDTO> newFeedVideos = fetchUserVideosEventResponse.getVideos().stream().map(
                userVideoDTO ->
                new VideoDTO(
                        userVideoDTO.getVideoId(),
                        userVideoDTO.getUserId(),
                        userVideoDTO.getBucketName(),
                        userVideoDTO.getCaption(),
                        userVideoDTO.getUploadTime(),
                        userVideoDTO.getTags(),
                        userVideoDTO.getDurationSeconds()
                )
        ).collect(Collectors.toList());

        updatedFeedOfTargetUser.addAll(newFeedVideos);  // add new videos to the list

        // remove duplicates based on videoId
       HashSet<String> videoIdsMap = new HashSet<>();
       List<VideoDTO> finalFeed = new ArrayList<>();// this will have the final feed

       for(VideoDTO videoDTO: updatedFeedOfTargetUser){
           String videoId = videoDTO.getVideoId();

           if(!videoIdsMap.contains(videoId)){ // only add unseen videos
               finalFeed.add(videoDTO);
               videoIdsMap.add(videoId);
           }
       }

        // Sort needed because it is a feed
        finalFeed.sort(Comparator.comparing(VideoDTO::getUploadTime).reversed());
        // finally store the new feed of target usr in cache
        feedCacheService.cacheFeedItems(targetUserId, finalFeed);

    }



}
