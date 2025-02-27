package com.automation.framework.camera;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CapturePhoto {
    public static void main(String[] args) {
        // Open the webcam
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        // Capture the image
        BufferedImage image = webcam.getImage();

        // Save the image to a file
        try {
            ImageIO.write(image, "PNG", new File("C:\\Users\\srinu13587\\Pictures\\captured_image.png"));
            System.out.println("Image captured and saved as captured_image.png");
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }

        // Close the webcam
        webcam.close();
    }
}
