package com.automation.framework.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WebcamVideoCaptureSwing {

    public static void main(String[] args) {
        // Create and open the webcam
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        // Create a JFrame to display the video
        JFrame frame = new JFrame("Webcam Video Capture");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);

        // Create a JPanel for displaying video
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Capture frame from webcam
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    // Draw the captured image on the panel
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);

        // Start capturing and displaying frames
        while (true) {
            panel.repaint(); // Repaint the panel to update the video display
            try {
                Thread.sleep(33); // Approx 30 FPS (1000ms / 30 frames)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
