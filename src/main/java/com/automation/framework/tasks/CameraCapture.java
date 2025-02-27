/*
 * package com.automation.framework.tasks;
 * 
 * import org.opencv.core.Core; import org.opencv.core.Mat; import
 * org.opencv.imgcodecs.Imgcodecs; import org.opencv.videoio.VideoCapture;
 * 
 * public class CameraCapture {
 * 
 * public static void main(String[] args) {
 * 
 * 
 * try { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Initialize the
 * VideoCapture object to access the camera VideoCapture camera = new
 * VideoCapture(0); // 0 means the default camera
 * 
 * if (!camera.isOpened()) { System.out.println("Error: Camera not detected.");
 * return; } // Wait for 5 seconds before taking a photo
 * System.out.println("Waiting for 5 seconds..."); Thread.sleep(5000);
 * 
 * // Capture a frame Mat frame = new Mat(); camera.read(frame);
 * 
 * if (!frame.empty()) { System.out.println("Photo captured!");
 * 
 * // Save the image to file using Imgcodecs.imwrite String filePath =
 * "captured_photo.jpg"; boolean success = Imgcodecs.imwrite(filePath, frame);
 * 
 * if (success) { System.out.println("Saved to " + filePath); } else {
 * System.out.println("Failed to save the image."); } } else {
 * System.out.println("Failed to capture image."); }
 * 
 * camera.release(); } catch (InterruptedException e) { e.printStackTrace(); }
 * finally { // Release the camera resource
 * 
 * } } }
 */