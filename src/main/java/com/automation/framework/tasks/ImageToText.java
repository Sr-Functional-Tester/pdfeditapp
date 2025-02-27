package com.automation.framework.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageToText {
    public static String imageToText(String query) throws IOException {


    	String queryi = query.toLowerCase().trim();

        // Split the query to extract the file path
        String[] parts = queryi.split("image to text", 2);
        
        String imagePath = parts[1].trim(); // Remove any leading or trailing spaces

        // Validate the image file path
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            System.out.println("The provided image file does not exist: " + imagePath);
            return null;
        }

        try {
            // Create an instance of Tesseract
            Tesseract tesseract = new Tesseract();

            // Extract the tessdata folder from resources to a temporary location
            File tempTessDataDir = extractTessdata();

            // Set the datapath to the temporary location of tessdata
            tesseract.setDatapath(tempTessDataDir.getAbsolutePath());

            // Perform OCR on the image
            String extractedText = tesseract.doOCR(imageFile);

            // Prepare the output file path with the same name as the input file but with .txt extension
            String outputFilePath = imageFile.getParent() + File.separator + getOutputFileName(imageFile);
            File outputFile = new File(outputFilePath);

            // Write the extracted text to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(extractedText);
            }

            // Output success message
            System.out.println("OCR completed successfully. Output written to: " + outputFilePath);

        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * Extracts the tessdata folder from the classpath to a temporary directory.
     * @return the path to the temporary tessdata directory.
     */
    private static File extractTessdata() throws IOException {
        // The directory inside the resources where tessdata is located
        String tessdataDir = "/tessdata/";

        // Create a temporary directory for tessdata
        File tempDir = Files.createTempDirectory("tessdata").toFile();
        tempDir.deleteOnExit();

        // Extract the eng.traineddata (or any other language files) from the resources
        InputStream inputStream = ImageToText.class.getResourceAsStream(tessdataDir + "eng.traineddata");
        if (inputStream == null) {
            throw new FileNotFoundException("eng.traineddata not found in resources.");
        }

        // Copy the resource to the temporary directory
        File trainedDataFile = new File(tempDir, "eng.traineddata");
        try (OutputStream outputStream = new FileOutputStream(trainedDataFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Return the temporary directory containing the extracted files
        return tempDir;
    }

    /**
     * Generates the output file name by replacing the image file's extension with .txt.
     * @param imageFile the image file to generate the output file name for
     * @return the generated output file name (same name as input file, but with .txt extension)
     */
    private static String getOutputFileName(File imageFile) {
        String fileName = imageFile.getName();
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex > 0) {
            // Replace the extension with .txt
            return fileName.substring(0, extensionIndex) + ".txt";
        } else {
            // If the file has no extension, just add .txt
            return fileName + ".txt";
        }
    }
}
