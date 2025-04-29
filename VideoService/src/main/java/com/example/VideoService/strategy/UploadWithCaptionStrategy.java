package com.example.VideoService.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.VideoMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UploadWithCaptionStrategy implements UploadStrategy{
    @Autowired
    private final VideoMetaDataRepository videoMetaDataRepository;

    public UploadWithCaptionStrategy(VideoMetaDataRepository videoMetaDataRepository) {
        this.videoMetaDataRepository = videoMetaDataRepository;
    }

    @Override
    public VideoMetaData saveVideoMetaData(VideoDTO videoDTO, long sizeInBytes) {
        VideoMetaData videoMetaData = new VideoMetaData.Builder()
                .videoId(videoDTO.getVideoId())
                .caption(videoDTO.getCaption())
                .sizeBytes(sizeInBytes)
                .durationSeconds(0)  //todo  note: this needs an external library like FFMPEG for example
                .processedAt(LocalDateTime.now())
                .build();

        return videoMetaDataRepository.save(videoMetaData);
    }
}
