package com.example.News.Feed.Service.dto;

import java.util.List;
import java.util.UUID;

public class FetchUserVideosEventResponse {

    private UUID requestId;       // To match the request and response
    private UUID userId;          // The user whose videos were fetched
    private List<VideoDTO> videos; // The list of fetched videos


    // Required no-arg constructor for deserialization (using Jackson, for example)
    public FetchUserVideosEventResponse() {
    }

    // Constructor to populate the response
    public FetchUserVideosEventResponse(UUID requestId, UUID userId, List<VideoDTO> videos) {
        this.requestId = requestId;
        this.userId = userId;
        this.videos = videos;

    }

    // Getters and Setters
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }

}
