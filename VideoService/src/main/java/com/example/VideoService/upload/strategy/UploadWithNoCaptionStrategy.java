package com.example.VideoService.upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.UserVideo;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.UserVideoRepository;
import com.example.VideoService.repository.VideoMetaDataRepository;
import com.example.VideoService.util.FFmpegVideoDurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

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
    public VideoMetaData saveVideoMetaData(VideoDTO videoDTO, MultipartFile videoFile) {

        double duration = FFmpegVideoDurationUtil.getVideoDuration(videoFile);


        VideoMetaData videoMetaData = new VideoMetaData.Builder()
                .videoId(videoDTO.getVideoId())
                .sizeBytes(videoFile.getSize())
                .durationSeconds(duration)
                .processedAt(Instant.now())
                .bucketName(videoDTO.getBucketName())
                .build();

        return videoMetaDataRepository.save(videoMetaData);
    }

    @Override
    public UserVideo saveUserVideo(VideoDTO videoDTO, MultipartFile videoFile) {
        if(videoDTO.getUploadTime()==null)
            videoDTO.setUploadTime(Instant.now());



        UserVideo userVideo = new UserVideo();

        UserVideo.UserVideoKey key = new UserVideo.UserVideoKey(
                UUID.fromString(videoDTO.getUserId()),
                videoDTO.getUploadTime()
        );
        userVideo.setKey(key);
        userVideo.setVideoId(videoDTO.getVideoId());
        double duration = FFmpegVideoDurationUtil.getVideoDuration(videoFile);
        userVideo.setDurationSeconds(duration);
        userVideo.setSizeBytes(videoFile.getSize());
        userVideo.setBucketName(videoDTO.getBucketName());

        return userVideoRepository.save(userVideo);



    }
}
