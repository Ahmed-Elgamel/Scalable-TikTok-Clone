package com.example.News.Feed.Service.sort;

import com.example.News.Feed.Service.dto.VideoDTO;

import java.util.List;

public interface ISortStrategy {
    List<VideoDTO> sort(List<VideoDTO> videos);
    String getStrategyName(); // used to identify the strategy dynamically

}