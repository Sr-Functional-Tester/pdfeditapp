package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class SimpleTextExtractionTest {

    public static void main(String[] args) throws TikaException {
        try {
            String inputFilePath = "C:\\Users\\srinu8963\\Downloads\\sample.docx";  
            
            // First, attempt to read and print the file content using BufferedReader
            try {
                // Create a BufferedReader to read the file
                BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                
                String line;
                // Read the file line by line and print content
                while ((line = reader.readLine()) != null) {
                    System.out.println("Read Line: " + line);  // Print each line of the file
                }
                
                // Close the reader
                reader.close();
            } catch (IOException e) {
                System.err.println("Error reading the file with BufferedReader: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Now, try extracting text using Tika
            Tika tika = new Tika();
            
            // Try using Tika to extract text from the file
            String extractedText = tika.parseToString(new File(inputFilePath));
            
            // Check if extracted text is empty
            if (extractedText.isEmpty()) {
                System.out.println("Tika could not extract any text from the file.");
            } else {
                System.out.println("Extracted Text using Tika: " + extractedText);
            }
            
        } catch (IOException | TikaException e) {
            e.printStackTrace();
        }
    }
}
