package com.example.VideoService.upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.UserVideo;
import com.example.VideoService.model.VideoMetaData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface UploadStrategy {
    VideoMetaData saveVideoMetaData(VideoDTO videoDTO, MultipartFile videoFile);
    UserVideo saveUserVideo(VideoDTO videoDTO, MultipartFile videoFile);
}
