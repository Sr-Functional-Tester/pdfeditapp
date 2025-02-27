package com.automation.framework.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddLogoToVideo {
    public static void main(String[] args) {
        String videoPath = "C:/Users/srinu13587/Pictures/one.mp4";  // Path to your input video
        String logoPath = "C:/Users/srinu13587/Pictures/dirty_police_mind.png";          // Path to your logo image
        String outputPath = "C:/Users/srinu13587/Pictures/output_video.mp4"; // Path for the output video
        String ffmpegResourcePath = "src/main/resources/ffmpeg.exe";
        String tempFfmpegPath = "C:/Users/srinu13587/Pictures/ffmpeg.exe";  
        // FFmpeg command to overlay logo on video
        // Make sure to split the command into individual arguments
       // String command = "ffmpeg -y -i " +videoPath+ " -y -i " +logoPath+ " -filter_complex overlay=W-w-10:H-h-10 -codec:a copy " +outputPath;
        String command = "ffmpeg -y -i " + videoPath + " -i " + logoPath + " -filter_complex overlay=W-w-10:10 -codec:a copy " + outputPath;

        // Step 1: Copy ffmpeg.exe to temporary location
        //copyFFmpegToTempLocation(ffmpegResourcePath, tempFfmpegPath);
        
        String directoryPath = "C:/Users/srinu13587/Pictures"; // Path to the directory where the video and logo are stored

      

       
     // Enclose paths with spaces in double quotes to handle spaces in file paths
      //  String command = "ffmpeg -y -i " + videoPath + " -i " + logoPath + " -filter_complex overlay=W-w-10:H-h-10 -c:v libx264 -c:a aac -strict experimental " + outputPath;

        System.out.println(command);
        try {
        	Thread.sleep(4000);
            // Run the FFmpeg command
        	ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/c","cd /d " + directoryPath + " && " + command);            Process process = processBuilder.start();
            try {
            int exitCode = process.waitFor();
            System.out.println(exitCode);
            }catch(Exception e) {
            	e.printStackTrace();
            }
//            if (exitCode == 0) {
//            	System.out.println("Video with logo has been saved to: " + outputPath);
//            }
            
            
            // Step 3: Remove the temporary ffmpeg.exe (optional)
           // deleteTempFfmpeg(tempFfmpegPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
