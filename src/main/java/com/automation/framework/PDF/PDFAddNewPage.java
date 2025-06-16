package com.automation.framework.PDF;

import java.io.File;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter; 

public class PDFAddNewPage {

    public static void addPdfPage(int pageNo, String sourcePdfPath) throws IOException {
        // Check if the source file exists
        File sourceFile = new File(sourcePdfPath);
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

        // Open the existing PDF document to read and write
        PdfReader reader = new PdfReader(sourcePdfPath);
        PdfWriter writer = new PdfWriter(destPdfPath);

        // Create PdfDocument for writing new page
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        // Add a new page (at specified page number or end)
        if (pageNo != 0) {
            pdfDocument.addNewPage(pageNo);
        }
        else {
            pdfDocument.addNewPage();
        }

        // Close the PDF document
        pdfDocument.close();
    }
    
    public static void addPdfPagesInRange(Integer startPage, Integer endPage, int numberOfPages, String sourcePdfPath) throws IOException {
        // Check if the source file exists
        File sourceFile = new File(sourcePdfPath);
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

        // Open the existing PDF document to read and write
        PdfReader reader = new PdfReader(sourcePdfPath);
        PdfWriter writer = new PdfWriter(destPdfPath);

        // Create PdfDocument for writing the new pages
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        // Create a temporary PdfDocument to copy the content
        PdfDocument tempDocument = new PdfDocument(new PdfWriter(destPdfPath));

        // If startPage and endPage are not provided, append the new pages at the end
        if (startPage == null || endPage == null) {
            // Copy all existing pages from the source document to the temp document
            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
                pdfDocument.copyPagesTo(i, i, tempDocument);
            }

            // Insert the new pages at the end of the document
            for (int i = 0; i < numberOfPages; i++) {
                tempDocument.addNewPage();
            }
        } else {
            // Copy pages from the start of the document to the startPage (before the insertion range)
            for (int i = 1; i < startPage; i++) {
                pdfDocument.copyPagesTo(i, i, tempDocument);
            }

            // Insert new pages between the start and end range
            for (int i = 0; i < numberOfPages; i++) {
                tempDocument.addNewPage();
            }

            // Copy the remaining pages after the inserted pages
            for (int i = endPage + 1; i <= pdfDocument.getNumberOfPages(); i++) {
                pdfDocument.copyPagesTo(i, i, tempDocument);
            }
        }

        // Close both documents
        pdfDocument.close();
        tempDocument.close();
    }

}
