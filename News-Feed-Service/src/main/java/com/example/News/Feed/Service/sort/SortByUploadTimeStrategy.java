package com.example.News.Feed.Service.sort;

import com.example.News.Feed.Service.dto.VideoDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component

public class SortByUploadTimeStrategy implements ISortStrategy {
    @Override
    public List<VideoDTO> sort(List<VideoDTO> videos) {
        return videos.stream()
                .sorted(Comparator.comparing(VideoDTO::getUploadTime).reversed()) // latest first
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "uploadTime";
    }
}
