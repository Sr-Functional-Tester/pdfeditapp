package com.automation.framework.PDF;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

public class PDFDeletePage {

    // Method to delete pages based on a list of page numbers
    public static void deletePdfPages(File sourcePdfPath, List<Integer> pagesToDelete ) throws Exception { 	
        try {      	
            File sourceFile = sourcePdfPath;
            if (!sourceFile.exists()) {
                throw new IOException("Source PDF file does not exist: " + sourcePdfPath);
            }

            // Check if the file is a valid PDF
            try {
                // Try to open the file using PdfReader to validate the PDF format
                PdfReader reader = new PdfReader(sourcePdfPath);
                reader.close();  // If this succeeds, it's a valid PDF
            } catch (IOException e) {
                throw new IOException("Invalid PDF file: " + sourcePdfPath, e);
            }

            // Extract the directory and file name from sourcePdfPath
            String directoryPath = sourceFile.getParent(); // Get directory of the source PDF
            String fileNameWithoutExtension = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.')); // Get the file name without extension

            // Step 2: Generate the destination path with "_output" suffix
            String destPdfPath = directoryPath + File.separator + fileNameWithoutExtension + "_output.pdf";
            
            // Open the original PDF using PdfReader (read-only mode)
            PdfReader reader = new PdfReader(sourcePdfPath);
            // Create a PdfWriter for the new output PDF (write-only mode)
            PdfWriter writer = new PdfWriter(destPdfPath);

            // Create a PdfDocument object for the source PDF (input)
            PdfDocument pdfDocument = new PdfDocument(reader);
            // Create a PdfDocument object for the destination PDF (output)
            PdfDocument newPdf = new PdfDocument(writer);

            // Get the total number of pages in the original PDF
            int totalPages = pdfDocument.getNumberOfPages();

            // Loop through all pages in the source PDF
            for (int i = 1; i <= totalPages; i++) {
                if (!pagesToDelete.contains(i)) {
                    // Copy each page to the new PDF except the ones to delete
                    pdfDocument.copyPagesTo(i, i, newPdf);
                }
            }

            // Close both documents
            pdfDocument.close();
            newPdf.close();

            System.out.println("Pages " + pagesToDelete + " deleted successfully.");
        } catch (Exception e) {
        	throw new Exception("error", e);
        }
    }

    // Method to delete pages from a range (inclusive)
    public static void deletePagesInRange(String inputPdf, int startPage, int endPage) {
        try {
        	 File sourceFile = new File(inputPdf);
             if (!sourceFile.exists()) {
                 throw new IOException("Source PDF file does not exist: " + inputPdf);
             }

             // Check if the file is a valid PDF
             try {
                 // Try to open the file using PdfReader to validate the PDF format
                 PdfReader reader = new PdfReader(inputPdf);
                 reader.close();  // If this succeeds, it's a valid PDF
             } catch (IOException e) {
                 throw new IOException("Invalid PDF file: " + inputPdf, e);
             }

             // Extract the directory and file name from sourcePdfPath
             String directoryPath = sourceFile.getParent(); // Get directory of the source PDF
             String fileNameWithoutExtension = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.')); // Get the file name without extension

             // Step 2: Generate the destination path with "_output" suffix
             String destPdfPath = directoryPath + File.separator + fileNameWithoutExtension + "_output.pdf";
            // Open the original PDF using PdfReader (read-only mode)
            PdfReader reader = new PdfReader(inputPdf);
            // Create a PdfWriter for the new output PDF (write-only mode)
            PdfWriter writer = new PdfWriter(destPdfPath);

            // Create a PdfDocument object for the source PDF (input)
            PdfDocument pdfDocument = new PdfDocument(reader);
            // Create a PdfDocument object for the destination PDF (output)
            PdfDocument newPdf = new PdfDocument(writer);

            // Get the total number of pages in the original PDF
            int totalPages = pdfDocument.getNumberOfPages();

            // Loop through all pages in the source PDF
            for (int i = 1; i <= totalPages; i++) {
                if (i < startPage || i > endPage) {
                    // Copy pages that are outside the range to the new PDF
                    pdfDocument.copyPagesTo(i, i, newPdf);
                }
            }

            // Close both documents
            pdfDocument.close();
            newPdf.close();

            System.out.println("Pages from " + startPage + " to " + endPage + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void deletePage(String sourcePdfPath) throws Exception {
		String str[] = sourcePdfPath.split(",");

		if (str[0] != null && str[0].contains("-")) {
			String pages[] = str[0].trim().split("-");
			deletePagesInRange(sourcePdfPath, Integer.parseInt(pages[0]), Integer.parseInt(pages[1]));
		} else {
			List<Integer> pagesToDelete = Arrays.asList(Integer.parseInt(str[0].trim()));
			File sourceFile = new File(str[0].trim());
			deletePdfPages(sourceFile, pagesToDelete);
		}
	}
}
