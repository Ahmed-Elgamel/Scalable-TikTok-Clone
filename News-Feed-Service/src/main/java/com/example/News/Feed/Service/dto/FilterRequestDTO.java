package com.example.News.Feed.Service.dto;

import java.util.List;

public class FilterRequestDTO {
    private List<String> tags;
    private Double durationSeconds;

    public List<String> getTags() {
        return tags;
    }

    public void setTags( List<String> tags) {
        this.tags = tags;
    }

    public Double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
