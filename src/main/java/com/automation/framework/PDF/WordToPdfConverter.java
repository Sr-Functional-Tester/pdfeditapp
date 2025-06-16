package com.automation.framework.PDF;

import java.io.FileInputStream;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordToPdfConverter {

    private static final float MARGIN = 25;
    private static final float Y_START = 750;
    private static final float LINE_SPACING = 15;
    private static final float PAGE_WIDTH = 595; // A4 width in PDF
    private static final int TAB_SIZE = 4; // Number of spaces for a tab character
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - 2 * MARGIN; // Allow some margin space

    public static void main(String args[]) throws Exception {
    	String a = WordToPdfConverter.convertDocxToPdf("/home/liveuser/Downloads/aa.docx");
    }
    public static String convertDocxToPdf(String inputQuery) throws Exception {
    	
    	//String wordFilePath = extractFilePath(inputQuery);
    	
    	String wordFilePath = inputQuery;
    	
    	// Split the string at the last occurrence of the backslash
    	String[] parts = wordFilePath.split("(?<=\\\\)(?=[^\\\\]*$)");

    	// The first part is the path
    	String outputPath = parts[0];

    	// Extract the file name (before the dot)
    	String fileNameWithExtension = parts[1];
    	String fileName = fileNameWithExtension.split("\\.")[0]; 
        String outputFile = outputPath+"\\"+fileName+".pdf";

        // Load the DOCX file using Apache POI
        FileInputStream docxFile = new FileInputStream(wordFilePath);
        XWPFDocument document = new XWPFDocument(docxFile);

        // Create a PDF document using PDFBox
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, Y_START);

        // Loop through the paragraphs in the DOCX document
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        float yPosition = Y_START;

        for (XWPFParagraph paragraph : paragraphs) {
            StringBuilder paragraphText = new StringBuilder();
            for (XWPFRun run : paragraph.getRuns()) {
                paragraphText.append(run.toString());
            }

            // Preserve newlines as they are
            String text = paragraphText.toString();

            // Split text into lines that fit within the page width, but without modifying line breaks
            String[] lines = wrapTextToFitWidth(text, contentStream, MAX_TEXT_WIDTH);

            // Write the lines to the PDF
            for (String line : lines) {
                if (yPosition <= LINE_SPACING) {
                    // Close the current content stream before adding a new page
                    contentStream.endText();
                    contentStream.close();

                    // Add a new page if the content exceeds the page's height
                    pdfDocument.addPage(new PDPage());
                    page = pdfDocument.getPage(pdfDocument.getNumberOfPages() - 1);
                    contentStream = new PDPageContentStream(pdfDocument, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, Y_START);
                    yPosition = Y_START; // Reset Y position for new page
                }

                // Add the line to the PDF
                contentStream.showText(line);
                yPosition -= LINE_SPACING;
                contentStream.newLineAtOffset(0, -LINE_SPACING); // Move to the next line
            }
        }

        // Finish writing the content to PDF and close the stream
        contentStream.endText();
        contentStream.close();

        // Save the PDF document
        pdfDocument.save(outputFile);
        pdfDocument.close();

        System.out.println("pdf file created in path "+outputFile);
        return "success";
    }

    /**
     * Wraps text to fit within the specified maximum width.
     * @param text The text to wrap.
     * @param contentStream The PDF content stream.
     * @param maxWidth The maximum width for text on a page.
     * @return An array of wrapped lines.
     */
    private static String[] wrapTextToFitWidth(String text, PDPageContentStream contentStream, float maxWidth) throws Exception {
        // Split the text by line breaks first, preserving them
        String[] paragraphs = text.split("\n");

        // Now, split each paragraph by word to handle wrapping individually
        StringBuilder wrappedText = new StringBuilder();
        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");
            StringBuilder currentLine = new StringBuilder();
            float currentWidth = 0;

            for (String word : words) {
                // Measure the width of the word
                float wordWidth = PDType1Font.HELVETICA.getStringWidth(word + " ") / 1000 * 12; // width of the word in points

                // If the word fits on the current line, add it
                if (currentWidth + wordWidth <= maxWidth) {
                    currentLine.append(word).append(" ");
                    currentWidth += wordWidth;
                } else {
                    // If the word doesn't fit, start a new line
                    wrappedText.append(currentLine.toString().trim()).append("\n");
                    currentLine = new StringBuilder(word + " ");
                    currentWidth = wordWidth;
                }
            }

            // Add the last line if it exists
            if (currentLine.length() > 0) {
                wrappedText.append(currentLine.toString().trim());
            }

            // Separate paragraphs with a newline
            wrappedText.append("\n");
            
        }

        // Return the wrapped text as an array of lines, preserving the original paragraph breaks
        wrappedText.append(" ");
        return wrappedText.toString().split("\n");
    }
    
    public static String extractFilePath(String input) {
        // Regular expression to match paths with dynamic drive letters (C:, D:, etc.)
        String regex = "[A-Za-z]:\\\\[\\w\\\\]+\\.docx";  // Pattern to match any drive (C:, D:, etc.) and path
        
        // If input matches the pattern, return the file path
        if (input.matches(".*" + regex + ".*")) {
            return input.replaceAll(".*(" + regex + ").*", "$1");
        }
        return null;  // Return null if no match is found
    }
}
