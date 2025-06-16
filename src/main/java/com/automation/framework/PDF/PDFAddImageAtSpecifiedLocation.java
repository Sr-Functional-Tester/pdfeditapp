package com.automation.framework.PDF;

import java.io.File;
import java.io.IOException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

public class PDFAddImageAtSpecifiedLocation {

	public static void addImageBackground(String BestBlufferAndBlueGreen) throws IOException {
		// blue-green
		String str[] = BestBlufferAndBlueGreen.split(",");
		int pageNo = Integer.parseInt(str[0].trim());
		int subx = Integer.parseInt(str[1].trim());
		int suby = Integer.parseInt(str[2].trim());
		String imagePath = str[3].trim();
		String sourcePdfPath = str[4].trim();
		
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
		
		String pdfInputPath = sourcePdfPath;
		String pdfOutputPath = destPdfPath;
		

		// Step 1: Open existing PDF document
		PdfReader reader = new PdfReader(pdfInputPath);
		PdfWriter writer = new PdfWriter(pdfOutputPath);
		PdfDocument pdfDocument = new PdfDocument(reader, writer);

		// Step 2: Load the image (This will be used as a background)
		ImageData image = ImageDataFactory.create(imagePath);

		PdfCanvas canvas = null;

		PdfPage page = pdfDocument.getPage(pageNo);
		canvas = new PdfCanvas(page);

		float a4Width = 420.0f; // A4 page width in points
		float a4Height = 720.0f; // A4 page height in points

		// Center coordinates
		float x = a4Width / 2;
		float y = a4Height / 2;

		canvas.addImageAt(image, x - subx, y - suby, false);
		// }

		// Close the document
		pdfDocument.close();
	}

	public static void main(String[] args) {
		try {
			String sourcePdfPath = "C:\\\\Users\\\\srinu13587\\\\Documents\\\\225669.pdf";
			String destPdfPath = "C:\\\\Users\\\\srinu13587\\\\Documents\\\\modified_225669.pdf";
			String imagePath = "C:\\\\\\\\Users\\\\\\\\srinu13587\\\\\\\\Documents\\\\\\\\image.png";

			//addImageBackground(2, 0, 240, sourcePdfPath, destPdfPath, imagePath);

			System.out.println("Image background added successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
