package com.example.News.Feed.Service.dto;

import javax.xml.crypto.dom.DOMCryptoContext;

public class FilterRequestDTO {
    private String tag;
    private Double durationSeconds;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
