package com.automation.framework.PDF;

import java.io.IOException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

public class PDFAddImageWaterMark {

	public static void addImageBackground(String pdfInputPath, String pdfOutputPath, String imagePath) throws IOException {

	    // Step 1: Open existing PDF document
	    PdfReader reader = new PdfReader(pdfInputPath);
	    PdfWriter writer = new PdfWriter(pdfOutputPath);
	    PdfDocument pdfDocument = new PdfDocument(reader, writer);

	    // Step 2: Load the image (This will be used as a background logo)
	    ImageData image = ImageDataFactory.create(imagePath);

	    // Step 3: Add image as background logo on each page
	    int numberOfPages = pdfDocument.getNumberOfPages();
	    for (int i = 1; i <= numberOfPages; i++) {
	        PdfPage page = pdfDocument.getPage(i);
	        
	        // Step 4: Get the canvas for the current page
	        PdfCanvas canvas = new PdfCanvas(page);
	        
	        float a4Width = 420.0f;  // A4 page width in points
	        float a4Height = 720.0f; // A4 page height in points

	        // Center coordinates
	        float x = a4Width / 2;
	        float y = a4Height / 2;

	        // Step 7: Create an extended graphics state to control opacity
	        PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.15f); // Set opacity (optional)

	        // Step 8: Apply the graphics state to the canvas
	        canvas.setExtGState(gs1);

	        // Step 9: Draw the image on the canvas as a background, centered
	        canvas.addImageAt(image, x, y, false); // Use scale to place the image at the right size and position

	        // Optional: Reset graphics state to default
	        canvas.setExtGState(new PdfExtGState().setFillOpacity(1.0f)); // Reset opacity to normal
	    }

	    // Step 10: Close the PDF document
	    pdfDocument.close();
	}


    public static void main(String[] args) {
        try {
            String sourcePdfPath = "C:\\Users\\srinu13587\\Documents\\225669.pdf";  // Path to the source PDF
            String destPdfPath = "C:\\Users\\srinu13587\\Documents\\modified_225669.pdf";  // Output path for the modified PDF
            String imagePath = "C:\\Users\\srinu13587\\Documents\\image.png";  // Path to the background image

            addImageBackground(sourcePdfPath, destPdfPath, imagePath);

            System.out.println("Logo background added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
