package com.example.VideoService.upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.UserVideo;
import com.example.VideoService.model.VideoMetaData;

public interface UploadStrategy {
    VideoMetaData saveVideoMetaData(VideoDTO videoDTO, long sizeInBytes);
    UserVideo saveUserVideo(VideoDTO videoDTO, long sizeInBytes);
}
