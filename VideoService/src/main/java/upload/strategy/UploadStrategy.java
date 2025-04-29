package upload.strategy;

import com.example.VideoService.dto.VideoDTO;
import com.example.VideoService.model.VideoMetaData;
import com.example.VideoService.repository.VideoMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface UploadStrategy {
    VideoMetaData saveVideoMetaData(VideoDTO videoDTO, long sizeInBytes);
}
