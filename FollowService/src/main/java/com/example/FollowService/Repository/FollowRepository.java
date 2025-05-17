package com.example.FollowService.Repository;

import com.example.FollowService.Model.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends MongoRepository<Follow, String> {
    List<Follow> findByFolloweeId(String followeeId);
    Optional<Follow> findByFollowerIdAndFolloweeId(String followerId, String followeeId);
    List<Follow> findByFollowerId(String followerId);

}
