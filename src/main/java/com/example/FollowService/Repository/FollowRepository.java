package com.example.FollowService.Repository;

import com.example.FollowService.Model.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FollowRepository extends MongoRepository<Follow, String> {
    List<Follow> findByFolloweeId(String followeeId);
    Follow findByFolloweeIdAndFollowerId(String followeeId,String followerId);
}
