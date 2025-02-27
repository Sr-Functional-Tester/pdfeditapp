package com.automation.framework.PDF;

import java.io.IOException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

public class PDFAddImageAtSpecifiedLocation {

    public static void addImageBackground(int pageNo, int subx, int suby, String pdfInputPath, String pdfOutputPath, String imagePath) throws IOException {

        // Step 1: Open existing PDF document
        PdfReader reader = new PdfReader(pdfInputPath);
        PdfWriter writer = new PdfWriter(pdfOutputPath);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        // Step 2: Load the image (This will be used as a background)
        ImageData image = ImageDataFactory.create(imagePath);


        // Step 3: Add image as background to each page
        int numberOfPages = pdfDocument.getNumberOfPages();
        PdfCanvas canvas=null;
      //  for (int i = 1; i <= numberOfPages; i++) {
            PdfPage page = pdfDocument.getPage(2);
            float pageWidth = page.getPageSize().getWidth();
            float pageHeight = page.getPageSize().getHeight();

            // Create a PdfCanvas to draw the image on the page
            canvas = new PdfCanvas(page);

            // Position the image at the bottom-left corner of the page and scale it to fit the page
            // We do not use scaling parameters, but position the image at the bottom-left (0, 0) and the page size
            float a4Width = 420.0f;  // A4 page width in points
	        float a4Height = 720.0f; // A4 page height in points

	        // Center coordinates
	        float x = a4Width / 2;
	        float y = a4Height / 2;
	        
            canvas.addImageAt(image, x-subx, y-suby, false);
       // }

        // Close the document
        pdfDocument.close();
    }

    public static void main(String[] args) {
        try {
            String sourcePdfPath = "C:\\\\Users\\\\srinu13587\\\\Documents\\\\225669.pdf";
            String destPdfPath = "C:\\\\Users\\\\srinu13587\\\\Documents\\\\modified_225669.pdf";
            String imagePath = "C:\\\\\\\\Users\\\\\\\\srinu13587\\\\\\\\Documents\\\\\\\\image.png";

           addImageBackground(2, 0, 240, sourcePdfPath, destPdfPath, imagePath);

            System.out.println("Image background added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
