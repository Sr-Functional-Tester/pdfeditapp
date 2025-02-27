package com.automation.framework.camera;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoRecording {

    private boolean stopRecording = false;

    public static void main(String[] args) throws InterruptedException {
        VideoRecording videoWriter = new VideoRecording();
        videoWriter.startVideoRecording("1m");
    }

    private String startVideoRecording(String time) throws InterruptedException {
        File saveFile = new File("C:/Users/srinu13587/Pictures/saved.mp4");

        //Initialize media writer
        IMediaWriter writer = ToolFactory.makeWriter(saveFile.getAbsolutePath());

        //Set video recording size
        Dimension size = WebcamResolution.VGA.getSize();

        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);

        long start = System.currentTimeMillis();
        Webcam webcam = openWebcam(size);

        int iterations = (int) calculateIterations(time);

        if(iterations > 45000)
            return "Max time to record video is just 45 minutes";
        
        // Create a listener for the Escape key
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // Optional, to hide the window
        frame.setFocusable(true);  // Make sure the frame can focus
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    stopRecording = true; // Set the flag to stop recording
                    frame.dispose(); // Close the frame after pressing Escape
                }
            }
        });
        frame.setVisible(true);
        frame.requestFocusInWindow(); // Request focus for key events

        for (int i = 0; i < iterations && !stopRecording; i++) {
            BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
            
            IVideoPicture framePicture = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
            framePicture.setKeyFrame(i == 0);
            framePicture.setQuality(100);
            
            writer.encodeVideo(0, framePicture);
            
            Thread.sleep(20);
        }

        writer.close();
        return stopRecording ? "Recording stopped by user" : "Video recorded to the file: " + saveFile.getAbsolutePath();
    }

    private Webcam openWebcam(Dimension size) {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(size);
        webcam.open();
        return webcam;
    }
    
    public static double calculateIterations(String time) {
        // Default iterations per minute
        int iterationsPerMinute = 1000;

        // Check if the input ends with 'm' for minutes or 's' for seconds
        if (time.endsWith("m")) {
            // Remove the 'm' and parse the number of minutes
            int minutes = Integer.parseInt(time.replace("m", ""));
            return iterationsPerMinute * minutes;
        } else if (time.endsWith("s")) {
            // Remove the 's' and parse the number of seconds
            int seconds = Integer.parseInt(time.replace("s", ""));
            // Convert seconds to minutes (since iterations are based on minutes)
            return iterationsPerMinute * (seconds / 60.0); // Resulting in a fractional iteration count
        } else {
            // If the time format is invalid, you can throw an exception or return a default value
            throw new IllegalArgumentException("Invalid time format. Use 'm' for minutes or 's' for seconds.");
        }
    }
}
