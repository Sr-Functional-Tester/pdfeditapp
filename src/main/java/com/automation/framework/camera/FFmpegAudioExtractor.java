package com.automation.framework.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FFmpegAudioExtractor {

    public static void main(String[] args) {
        String videoFilePath = "C:/Users/srinu13587/Pictures/one.mp4";  // Path to the video file
        String audioFilePath = "C:/Users/srinu13587/Pictures/audio.wav";  // Path for the extracted audio
        String ffmpegResourcePath = "src/main/resources/ffmpeg.exe";  // Path to ffmpeg.exe in resources folder
        String tempFfmpegPath = "C:/Users/srinu13587/Pictures/ffmpeg.exe";  // Temporary location for ffmpeg.exe

        // Step 1: Copy ffmpeg.exe to temporary location
        copyFFmpegToTempLocation(ffmpegResourcePath, tempFfmpegPath);

        // Step 2: Extract audio from the video using FFmpeg
        extractAudio(videoFilePath, audioFilePath, tempFfmpegPath);

        // Step 3: Remove the temporary ffmpeg.exe (optional)
        deleteTempFfmpeg(tempFfmpegPath);
    }

    // Copy ffmpeg.exe from resources folder to temporary location
    public static void copyFFmpegToTempLocation(String resourcePath, String tempPath) {
        File resourceFile = new File(resourcePath);
        File tempFile = new File(tempPath);

        // Check if resource file exists
        if (!resourceFile.exists()) {
            System.err.println("ffmpeg.exe file not found in the specified resources path: " + resourcePath);
            return;
        }

        try (InputStream in = new FileInputStream(resourceFile);
             FileOutputStream out = new FileOutputStream(tempFile)) {

            // Create parent directories if they don't exist
            tempFile.getParentFile().mkdirs();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("ffmpeg.exe copied to temporary location: " + tempPath);
        } catch (IOException e) {
            System.err.println("Error copying ffmpeg.exe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Extracts audio from the video using FFmpeg
    public static void extractAudio(String videoFilePath, String audioFilePath, String ffmpegPath) {
        // FFmpeg command to extract audio (same as your command in CMD)
        String ffmpegCommand = ffmpegPath + " -y -i \"" + videoFilePath + "\" -vn -acodec pcm_s16le -ar 44100 -ac 2 \"" + audioFilePath + "\"";
        
        System.out.println(ffmpegCommand);

        try {
            // Execute the command in the command prompt
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand);
            processBuilder.redirectErrorStream(true);  // Redirect error stream to normal output

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete and get the exit code
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Audio extracted successfully to: " + audioFilePath);
            } else {
                System.err.println("FFmpeg process failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Delete the temporary ffmpeg.exe after the process is done (optional)
    public static void deleteTempFfmpeg(String tempFfmpegPath) {
        File ffmpegFile = new File(tempFfmpegPath);
        if (ffmpegFile.exists() && ffmpegFile.delete()) {
            System.out.println("Temporary ffmpeg.exe deleted successfully.");
        } else {
            System.err.println("Error deleting temporary ffmpeg.exe.");
        }
    }
}
