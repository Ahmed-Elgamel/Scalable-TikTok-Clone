package com.example.News.Feed.Service.dto;

import org.apache.kafka.common.protocol.types.Field;

import java.util.List;
import java.util.UUID;

public class FeedDTO {
    String userId;              // user who requested the feed
    List<VideoDTO> videoDTOS;  // the videos of his/her feed

    public FeedDTO(String userId, List<VideoDTO> videoDTOS) {
        this.userId = userId;
        this.videoDTOS = videoDTOS;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<VideoDTO> getVideoDTOS() {
        return videoDTOS;
    }

    public void setVideoDTOS(List<VideoDTO> videoDTOS) {
        this.videoDTOS = videoDTOS;
    }
}
