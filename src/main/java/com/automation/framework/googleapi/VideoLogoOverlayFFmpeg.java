package com.automation.framework.googleapi;
import java.io.IOException;

public class VideoLogoOverlayFFmpeg {

    public static void main(String[] args) {
        // Path to input video, output video, and logo image
        String inputVideo = "input_video.mp4";
        String outputVideo = "output_video_with_logo.mp4";
        String logoPath = "logo.png";

        // Define FFmpeg command to add logo to the video
        // -i: Input file
        // -filter_complex: Apply video filters to overlay the logo
        // -codec: Specifies video codec (copy here to avoid re-encoding)
        String ffmpegCommand = String.format(
                "ffmpeg -i %s -i %s -filter_complex \"overlay=10:10\" -codec:a copy %s",
                inputVideo, logoPath, outputVideo
        );

        try {
            // Run the FFmpeg command
            Process process = Runtime.getRuntime().exec(ffmpegCommand);
            int exitCode = process.waitFor();

            // Check if the process executed successfully
            if (exitCode == 0) {
                System.out.println("Logo successfully added to video!");
            } else {
                System.err.println("Error occurred while processing the video.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
