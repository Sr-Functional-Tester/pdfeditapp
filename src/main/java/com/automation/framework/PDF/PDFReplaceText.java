package com.automation.framework.PDF;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfcleanup.PdfCleaner;
import com.itextpdf.pdfcleanup.autosweep.CompositeCleanupStrategy;
import com.itextpdf.pdfcleanup.autosweep.RegexBasedCleanupStrategy;

public class PDFReplaceText {

    public static void changePdf(String inputString) throws Exception {
    	 // Check if the source file exists
    	//int pageNo, int fontSizen, String findText, String replaceText, String sourcePdfPath
    	String str[] = inputString.split(",");
    	int pageNo = Integer.parseInt(str[0].trim());
    	int fontSizen = Integer.parseInt(str[1].trim());
    	String findText = str[2].trim();
    	String replaceText = str[3].trim();	
    	String sourcePdfPath = str[4].trim();
        File sourceFile = new File(sourcePdfPath);
        
        System.out.println(pageNo+"-"+fontSizen+"-"+findText+"-"+replaceText+"-"+sourcePdfPath);
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
        
        PdfReader reader = new PdfReader(sourcePdfPath);
        PdfWriter writer = new PdfWriter(destPdfPath);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        int numberOfPages = pdfDocument.getNumberOfPages();

        // Check if the document has more than one page
        if (numberOfPages > 1) {
            // Move the second page to the first position
            pdfDocument.movePage(pageNo, 1);  // Move page 2 to position 1
        }

        // Set up cleanup strategy to find the text
        CompositeCleanupStrategy strategy = new CompositeCleanupStrategy();
        strategy.add(new RegexBasedCleanupStrategy(findText).setRedactionColor(ColorConstants.WHITE));
        PdfCleaner.autoSweepCleanUp(pdfDocument, strategy);

        // Choose a font for measuring the text
       // PdfFont font = PdfFontFactory.createFont("Times-Bold");
        PdfFont font = PdfFontFactory.createFont("Times-Roman");

        
        // Collect the locations
        Collection<IPdfTextLocation> location1 = strategy.getResultantLocations();
        location1.forEach(n -> System.out.println(n));

        // Iterate over each location where text needs to be replaced
        for (IPdfTextLocation location : strategy.getResultantLocations()) {
            int pageNumber = location.getPageNumber();
            PdfPage page = pdfDocument.getPage(pageNumber + 1);
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), page.getDocument());

            // Get the rectangle of the location to replace the text
            float x = location.getRectangle().getX();
            float y = location.getRectangle().getY();
            float width = location.getRectangle().getWidth();
            float height = location.getRectangle().getHeight();

            // Replacement text
            String replacementText = replaceText;
            String fontSize1 = fontSizen+"f";
            float fontSize = Float.parseFloat(fontSize1);

            // Calculate the width of the replacement text using the font size
            float textWidth = font.getWidth(replacementText) * fontSize / 1000f;

            // Adjust the bounding box width to fit the text
            if (textWidth > width) {
                width = textWidth;  // Increase bounding box width to fit the text
            } else if (textWidth < width) {
                width = textWidth;  // Shrink bounding box if the text is smaller
            }

            // Calculate the y position by adjusting for font size height
            float adjustedY = y + height - fontSize;

            // Create a canvas and set the font size
            try (Canvas canvas = new Canvas(pdfCanvas, location.getRectangle())) {
                canvas.setFont(font).setFontSize(fontSize);

                // Set the paragraph with the replacement text
                Paragraph paragraph = new Paragraph(replacementText)
                        .setFontSize(fontSize)
                        .setMarginLeft(0)
                        .setFont(font)
                        .setFixedPosition(x, adjustedY-4, width);
                System.out.println(x+"-"+(adjustedY-100)+"-"+width);

                // Add the paragraph to the canvas
                canvas.add(paragraph);
            }
        }

        if (numberOfPages > 1) {
            // Move the second page to the first position
            pdfDocument.movePage(1, pageNo);  // Move page 2 to position 1
        }
        pdfDocument.close();
        System.out.println("done");
    }
    
//    public static void main(String[] args) throws Exception {
//    	//PDFReplaceText.changePdf(2, 11, "Medium","Large", "/mnt/abc.pdf");
//    }
}
