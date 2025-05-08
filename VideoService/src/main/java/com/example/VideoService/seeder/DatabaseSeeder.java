package com.example.VideoService.seeder;

import com.example.VideoService.dto.VideoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ResourceUtils;

@Service
public class DatabaseSeeder {

    @Value("${video.service.upload.url}")
    private String uploadUrl;  // URL for the upload endpoint

    private final OkHttpClient httpClient = new OkHttpClient();

    // Hardcoded userId
    List<String> userIds = Arrays.asList(
            "e4eaaaf2-d142-11e1-b3e4-080027620cdd",
            "f91f9423-0f8e-4ea3-9bd7-c9d2c1c7639e",
            "4cdbd23e-4425-4c1d-8f16-871b21a379ce",
            "01f5c999-cc15-4f7a-bb9d-91e16c5eb9fb",
            "abed0a2a-fc63-4ea0-8c76-c77e68fc4cb3",
            "693a90ec-d7c2-49cc-8914-6cd205934c49",
            "eef74026-03a0-4e5a-8d0d-78800e8cb5c2",
            "908f9a15-62f7-4f92-8cb1-148b12e2d80f",
            "3e17453c-bc38-41cf-878f-2f51134b3c02",
            "53c2f5f3-5577-482b-a058-b56c0dc0ea9a"
    );

    public String seedVideos() throws FileNotFoundException {

        List<File> files = List.of(
                new File("/opt/videos/video1.mp4"),
                new File("/opt/videos/video2.mp4"),
                new File("/opt/videos/video3.mp4"),
                new File("/opt/videos/video4.mp4"),
                new File("/opt/videos/video5.mp4")
        );


        List<String> captions = Arrays.asList(
                "caption 1",
                null,  // No caption for this one
                "caption 3"
        );

        // todo: will add tags to upload video request??
//        List<List> tags = List.of(
//                List.of(),
//                List.of(),
//                List.of(),
//                null
//        );

        for (int i = 0; i < userIds.size(); i++) {
            try {
                uploadVideo(files.get( (int)(Math.random()*files.size()) ), userIds.get(i),
                        captions.get((int)(Math.random()*captions.size()) ));
            } catch (Exception e) {
                System.out.println("Failed to upload video: " + e.getMessage());
            }
        }
        return "Seeded Microservice with videos successfully";
    }

    private void uploadVideo(File file, String userId, String caption) throws IOException {
        RequestBody videoBody = RequestBody.create(file, MediaType.parse("video/mp4"));

        // Construct JSON metadata
        String metadataJson = "{\"userId\": \"" + userId + "\"";
        if (caption != null) {
            metadataJson += ", \"caption\": \"" + caption + "\"";
        }
        metadataJson += "}";

        // ✅ Create RequestBody for metadata
        RequestBody metadataBody = RequestBody.create(metadataJson, MediaType.parse("application/json"));

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), videoBody)
                .addFormDataPart("metadata", null, metadataBody) // ✅ Correct way to add JSON as multipart
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("✅ Successfully uploaded: " + file.getName());
            } else {
                System.out.println("❌ Upload failed for: " + file.getName());
                System.out.println("Response: " + response.body().string());
            }
        }
    }
}
