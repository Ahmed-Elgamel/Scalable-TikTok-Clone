package com.example.News.Feed.Service.filter;

import com.example.News.Feed.Service.dto.FeedDTO;

import java.io.IOException;
import java.util.List;

public interface NewsFeedFilterCommand {
    FeedDTO execute() throws IOException;

}
