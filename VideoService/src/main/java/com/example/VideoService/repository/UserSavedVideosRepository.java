package com.example.VideoService.repository;

import com.example.VideoService.model.UserSavedVideo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.util.List;
import java.util.UUID;

public interface UserSavedVideosRepository extends CassandraRepository<UserSavedVideo, UserSavedVideo.UserSavedVideoKey> {
    List<UserSavedVideo> findByKeyUserId(UUID userId);
}
