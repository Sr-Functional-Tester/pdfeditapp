package com.automation.framework.PDF;

import com.itextpdf.kernel.pdf.*;
import java.util.List;

public class PDFDeletePage {

    // Method to delete pages based on a list of page numbers
    public static void deletePdfPages(String inputPdf, String outputPdf, List<Integer> pagesToDelete) {
        try {
            // Open the original PDF using PdfReader (read-only mode)
            PdfReader reader = new PdfReader(inputPdf);
            // Create a PdfWriter for the new output PDF (write-only mode)
            PdfWriter writer = new PdfWriter(outputPdf);

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
            e.printStackTrace();
        }
    }

    // Method to delete pages from a range (inclusive)
    public static void deletePagesInRange(String inputPdf, String outputPdf, int startPage, int endPage) {
        try {
            // Open the original PDF using PdfReader (read-only mode)
            PdfReader reader = new PdfReader(inputPdf);
            // Create a PdfWriter for the new output PDF (write-only mode)
            PdfWriter writer = new PdfWriter(outputPdf);

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

    public static void main(String[] args) {
        // Input and output PDF file paths
        String sourcePdfPath = "C:\\Users\\srinu13587\\Documents\\Computer_Applications_Sec_2024-25.pdf";
        String destPdfPath = "C:\\Users\\srinu13587\\Documents\\modified_test.pdf";
        
        // Delete specific pages (e.g., delete pages 2, 4, 6)
        deletePdfPages(sourcePdfPath, destPdfPath, List.of(2));

        // OR delete pages in a range (e.g., delete pages from 2 to 6)
         deletePagesInRange(sourcePdfPath, destPdfPath, 2, 6);
    }
}
