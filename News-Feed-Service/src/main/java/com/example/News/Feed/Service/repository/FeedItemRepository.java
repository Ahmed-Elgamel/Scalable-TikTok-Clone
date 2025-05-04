package com.example.News.Feed.Service.repository;

import com.example.News.Feed.Service.model.FeedItem;
import com.example.News.Feed.Service.model.FeedItem.FeedItemKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedItemRepository extends CassandraRepository<FeedItem, FeedItemKey> {

    // Find all feed items for a specific user, sorted by uploadTime descending (most recent first)
    List<FeedItem> findByKeyUserIdOrderByKeyUploadTimeDesc(String userId);

    // Optionally, find feed items by user ID and upload time if needed
    List<FeedItem> findByKeyUserIdAndKeyUploadTimeGreaterThan(String userId, java.time.Instant since);
}
