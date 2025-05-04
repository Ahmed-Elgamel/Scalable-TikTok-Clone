package com.example.VideoService.repository;

import com.example.VideoService.model.UserVideo;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface UserVideoRepository extends CassandraRepository<UserVideo,UserVideo.UserVideoKey> {
    List<UserVideo> findTop10ByKeyUserIdOrderByKeyUploadTimeDesc(UUID userId);

    // Query to find all videos by userId
    List<UserVideo> findByKeyUserId(UUID userId);
}
