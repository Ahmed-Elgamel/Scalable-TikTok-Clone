package com.example.VideoService.util;

import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.PointerPointer;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FFmpegVideoDurationUtil {

    static {
        avutil.av_log_set_level(avutil.AV_LOG_QUIET); // Suppress logs
    }

    /**
     * Get the duration of a video file in seconds.
     *
     * @param multipartFile Multipart video file
     * @return Duration in seconds
     */
    public static double getVideoDuration(MultipartFile multipartFile) {
        File tempFile = null;
        try {
            // Save the multipart file to a temporary file
            tempFile = File.createTempFile("upload_", ".mp4");
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                out.write(multipartFile.getBytes());
            }

            // FFmpeg read duration
            AVFormatContext formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, tempFile.getAbsolutePath(), null, null) != 0) {
                throw new RuntimeException("Failed to open video file");
            }

            // Get the stream info correctly without metadata() call
            if (avformat.avformat_find_stream_info(formatContext, (PointerPointer<?>) null) < 0) {
                throw new RuntimeException("Failed to retrieve video stream info");
            }

            double duration = formatContext.duration() / (double) avutil.AV_TIME_BASE;
            avformat.avformat_close_input(formatContext);

            return duration;

        } catch (IOException e) {
            throw new RuntimeException("Failed to process video file", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete(); // Cleanup the temporary file
            }
        }
    }
}
