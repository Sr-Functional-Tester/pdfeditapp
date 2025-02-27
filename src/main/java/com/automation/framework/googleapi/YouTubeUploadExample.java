package com.automation.framework.googleapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.api.services.youtube.model.VideoTopicDetails;

public class YouTubeUploadExample {

    private static YouTube youtubeService;
    
    private static final List<String> SCOPES = Arrays.asList(    
    	    "https://www.googleapis.com/auth/youtube.upload",  // For YouTube upload
    	    "https://www.googleapis.com/auth/youtube"  // For accessing user's YouTube account
    	);


    public static YouTube getYouTubeService(Credential credential) {
        // Build the YouTube service object
        return new YouTube.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("YouTube API Java Upload Example")
                .build();
    }

    public static void uploadVideo(Credential credential, String videoFilePath, String title, String description) throws IOException {
        // Set up the YouTube service
        youtubeService = getYouTubeService(credential);

        // Set video metadata (title, description, etc.)
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);

        // Set video status (public, private, etc.)
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");  // Options: "public", "unlisted", "private"

        // Set video content
        File videoFile = new File(videoFilePath);
        FileContent mediaContent = new FileContent("video/*", videoFile);
        
        VideoTopicDetails videoTopicDetails = new VideoTopicDetails();
        List<String> topicIds = new ArrayList<String>();
        topicIds.add("Republic Day 76th 2025");
//        topicIds.add("spring boot");
//        topicIds.add("spring");
//        topicIds.add("Java");
        videoTopicDetails.setTopicIds(topicIds);

       // VideoContentDetails videoContentDetails = new VideoContentDetails();
       // videoContentDetails.setCaption("Republic Day 76th 2025");
        Video video = new Video();
       // video.setContentDetails(videoContentDetails);
        video.setSnippet(snippet);
        video.setStatus(status);
       // video.sett
        // Prepare the request for uploading the video
        YouTube.Videos.Insert request = youtubeService.videos()
                .insert("snippet,statistics,status", null, mediaContent);

       

        // Execute the upload request
        Video video1 = request.execute();

        // Output the uploaded video ID
        System.out.println("Uploaded video ID: " + video1.getId());
    }

    public static void main(String[] args) throws Exception {
        // Assuming getCredential() method is available
        Credential credential = GoogleDriveAuthExample.getCredential(SCOPES);
        String videoFilePath = "C:\\Users\\srinu13587\\Pictures\\Camera Roll\\video.mp4";
        String title = "Republic Day 76th 2025";
        String description = "Republic Day 76th 2025";

        // Upload the video to YouTube
        uploadVideo(credential, videoFilePath, title, description);
    }
}
