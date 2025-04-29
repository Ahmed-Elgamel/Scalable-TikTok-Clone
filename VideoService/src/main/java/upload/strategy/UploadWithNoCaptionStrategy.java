package upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.VideoMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UploadWithNoCaptionStrategy implements UploadStrategy{
    @Autowired
    private final VideoMetaDataRepository videoMetaDataRepository;

    public UploadWithNoCaptionStrategy(VideoMetaDataRepository videoMetaDataRepository) {
        this.videoMetaDataRepository = videoMetaDataRepository;
    }

    @Override
    public VideoMetaData saveVideoMetaData(VideoDTO videoDTO, long sizeInBytes) {
        VideoMetaData videoMetaData = new VideoMetaData();

        videoMetaData.setVideoId(videoDTO.getVideoId());
        videoMetaData.setDurationSeconds(0);  //todo  note: this needs an external library like FFMPEG for example
        videoMetaData.setSizeBytes(sizeInBytes);
        videoMetaData.setProcessedAt(LocalDateTime.now());
        return videoMetaDataRepository.save(videoMetaData);
    }
}
