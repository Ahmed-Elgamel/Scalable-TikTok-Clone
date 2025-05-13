package com.example.VideoService.repository;

import com.example.VideoService.model.UserVideoRating;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.util.List;
import java.util.UUID;

public interface UserVideoRatingRepository extends CassandraRepository<UserVideoRating, UserVideoRating.UserVideoRatingKey> {
    List<UserVideoRating> findByKeyUserId(UUID userId);
}


