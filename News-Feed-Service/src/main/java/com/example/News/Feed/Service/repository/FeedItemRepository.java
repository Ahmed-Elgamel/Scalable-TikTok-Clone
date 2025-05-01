package com.example.News.Feed.Service.repository;

import com.example.News.Feed.Service.model.FeedItem;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface FeedItemRepository extends CassandraRepository<FeedItem, String> {
}
