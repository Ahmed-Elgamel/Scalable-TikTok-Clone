package com.example.FollowService.Repository;

import com.example.FollowService.Model.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowRepository extends MongoRepository<Follow, String> {
}
