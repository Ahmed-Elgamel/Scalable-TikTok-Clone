package com.example.VideoService.repository;

import com.example.VideoService.model.VideoMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoMetaDataRepository extends MongoRepository<VideoMetaData, String> {
    void deleteById(String videoId);
}
