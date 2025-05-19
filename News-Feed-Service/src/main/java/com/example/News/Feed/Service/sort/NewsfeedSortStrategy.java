package com.example.News.Feed.Service.sort;

import com.example.News.Feed.Service.dto.FeedDTO;

import java.io.IOException;

public interface NewsfeedSortStrategy {
    FeedDTO sort() throws IOException;
}
