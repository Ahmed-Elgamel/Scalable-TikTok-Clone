package com.example.VideoService.upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.UserVideo;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.UserVideoRepository;
import com.example.VideoService.repository.VideoMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class UploadWithNoCaptionStrategy implements UploadStrategy{
    @Autowired
    private final VideoMetaDataRepository videoMetaDataRepository;
    @Autowired
    private final UserVideoRepository userVideoRepository;

    public UploadWithNoCaptionStrategy(VideoMetaDataRepository videoMetaDataRepository, UserVideoRepository userVideoRepository) {
        this.videoMetaDataRepository = videoMetaDataRepository;
        this.userVideoRepository = userVideoRepository;
    }

    @Override
    public VideoMetaData saveVideoMetaData(VideoDTO videoDTO, long sizeInBytes) {


        VideoMetaData videoMetaData = new VideoMetaData.Builder()
                .videoId(videoDTO.getVideoId())
                .sizeBytes(sizeInBytes)
                .durationSeconds(0)  //todo  note: this needs an external library like FFMPEG for example
                .processedAt(Instant.now())
                .bucketName(videoDTO.getBucketName())
                .build();

        return videoMetaDataRepository.save(videoMetaData);
    }

    @Override
    public UserVideo saveUserVideo(VideoDTO videoDTO, long sizeInBytes) {
        if(videoDTO.getUploadTime()==null)
            videoDTO.setUploadTime(Instant.now());

        UserVideo userVideo = new UserVideo();

        UserVideo.UserVideoKey key = new UserVideo.UserVideoKey(
                UUID.fromString(videoDTO.getUserId()),
                videoDTO.getUploadTime()
        );
        userVideo.setKey(key);
        userVideo.setVideoId(videoDTO.getVideoId());
        userVideo.setDurationSeconds(0);
        userVideo.setSizeBytes(sizeInBytes);
        userVideo.setBucketName(videoDTO.getBucketName());

        return userVideoRepository.save(userVideo);



    }
}
