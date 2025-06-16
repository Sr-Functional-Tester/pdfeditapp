package com.automation.framework.PDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PDFMerger {
    public static String mergePdfs(String sourcePdfPath) throws IOException {
            // Convert the comma-separated input file paths to a list
    	//System.out.println(sourcePdfPath);
            String[] filesArray = sourcePdfPath.split(",");
            
            File sourceFile = new File(filesArray[0]);
            if (!sourceFile.exists()) {
                throw new IOException("Source PDF file does not exist: " + sourcePdfPath);
            }

            // Extract the directory and file name from sourcePdfPath
            String directoryPath = sourceFile.getParent(); // Get directory of the source PDF
            String fileNameWithoutExtension = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.')); // Get the file name without extension

            // Step 2: Generate the destination path with "_output" suffix
            String destPdfPath = directoryPath + File.separator + fileNameWithoutExtension + "_merged.pdf";

            
            List<String> fileList = new ArrayList<>();
            for (String file : filesArray) {
            	
                fileList.add(file.trim());  // Trim spaces around file names
            }

            // Validate the output file path
            File outputFile = new File(destPdfPath);
            if (outputFile.exists()) {
                System.out.println("Output file already exists. It will be overwritten.");
            }
             System.out.println(fileList.toString()+"---"+outputFile);
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
        
        return "success";
    }
}
