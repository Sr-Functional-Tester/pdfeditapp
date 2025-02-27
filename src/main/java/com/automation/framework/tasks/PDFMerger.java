package com.automation.framework.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PDFMerger {
    public static String mergePdfs(String query) {

         String input = query;

        // Check if the input matches the expected pattern
        if (input.startsWith("merge pdf files into")) {
            // Remove the "merge pdf files into" part
            String[] parts = input.substring(21).split(" from ", 2); // Splitting into output file and input files part

            if (parts.length < 2) {
                System.out.println("Invalid input format. Please provide both output file and input files.");
                return null;
            }

            String outputFilePath = parts[0].trim();  // Output file path
            String inputFilePaths = parts[1].trim(); // Input file paths (comma-separated)

            // Convert the comma-separated input file paths to a list
            String[] filesArray = inputFilePaths.split(",");
            List<String> fileList = new ArrayList<>();
            for (String file : filesArray) {
                fileList.add(file.trim());  // Trim spaces around file names
            }

            // Validate the output file path
            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                System.out.println("Output file already exists. It will be overwritten.");
            }

            // Create PDFMergerUtility instance
            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            // Add the PDF files in the specified order
            for (String filePath : fileList) {
                File pdfFile = new File(filePath);

                // Check if the file exists
                if (pdfFile.exists() && pdfFile.isFile()) {
                    try {
						pdfMerger.addSource(pdfFile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    System.out.println("File " + filePath + " not found.");
                    return null;
                }
            }

            try {
                // Merge the PDFs
                pdfMerger.setDestinationFileName(outputFile.getAbsolutePath());
                pdfMerger.mergeDocuments();

                System.out.println("PDF files merged successfully into " + outputFile.getAbsolutePath());

            } catch (IOException e) {
                System.out.println("An error occurred while merging PDFs: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid command format.");
        }
        return "success";
    }
}
