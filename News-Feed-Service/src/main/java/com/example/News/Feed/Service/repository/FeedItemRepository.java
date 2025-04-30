package com.example.News.Feed.Service.repository;

import com.example.News.Feed.Service.model.FeedItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedItemRepository extends MongoRepository<FeedItem, String> {
}
