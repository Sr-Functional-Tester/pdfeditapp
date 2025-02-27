package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class FileSToPdfConverter {

    private static final float MARGIN = 25;
    private static final float Y_START = 750;
    private static final float LINE_SPACING = 15;
    private static final float PAGE_WIDTH = 595; // A4 width in PDF
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - 2 * MARGIN;

    public static String convertToPdf(String inputFilePath) throws Exception {
        String extractedText = "";

        File file = new File(inputFilePath);
        String fileExtension = getFileExtension(file);

        // Extract text based on file type
        if (fileExtension.equals("txt")) {
            extractedText = readTextFile(file);
        } else if (fileExtension.equals("docx")) {
            extractedText = extractTextFromDocx(file);
        } else if (fileExtension.equals("pptx")) {
            extractedText = extractTextFromPptx(file);
        } else if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
            extractedText = extractTextFromExcel(file);
        } else {
            throw new Exception("Unsupported file format: " + fileExtension);
        }

        // Prepare the output PDF file
        String outputFile = inputFilePath.replaceFirst("[.][^.]+$", "") + ".pdf";

        // Create a PDF document
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, Y_START);

        // Wrap text to fit within the page width
        String[] lines = wrapTextToFitWidth(extractedText, contentStream, MAX_TEXT_WIDTH);

        float yPosition = Y_START;

        for (String line : lines) {
            if (yPosition <= LINE_SPACING) {
                // Start a new page if content exceeds page height
                contentStream.endText();
                contentStream.close();
                pdfDocument.addPage(new PDPage());
                page = pdfDocument.getPage(pdfDocument.getNumberOfPages() - 1);
                contentStream = new PDPageContentStream(pdfDocument, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, Y_START);
                yPosition = Y_START; // Reset Y position
            }

            // Add the line to the PDF
            contentStream.showText(line);
            yPosition -= LINE_SPACING;
            contentStream.newLineAtOffset(0, -LINE_SPACING); // Move to the next line
        }

        contentStream.endText();
        contentStream.close();
        pdfDocument.save(outputFile);
        pdfDocument.close();

        return outputFile;
    }

    private static String readTextFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();
        return content.toString();
    }

    private static String extractTextFromDocx(File file) throws Exception {
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));
        StringBuilder text = new StringBuilder();
        for (org.apache.poi.xwpf.usermodel.XWPFParagraph paragraph : document.getParagraphs()) {
            text.append(paragraph.getText()).append("\n\n");
        }
        document.close();
        return text.toString();
    }

    private static String extractTextFromPptx(File file) throws Exception {
        // Load the PowerPoint presentation
        org.apache.poi.xslf.usermodel.XMLSlideShow ppt = new org.apache.poi.xslf.usermodel.XMLSlideShow(new FileInputStream(file));
        StringBuilder text = new StringBuilder();

        // Iterate over slides in the PowerPoint presentation
        for (org.apache.poi.xslf.usermodel.XSLFSlide slide : ppt.getSlides()) {
            // Iterate over shapes in the slide
            for (org.apache.poi.xslf.usermodel.XSLFShape shape : slide.getShapes()) {
                // Check if the shape is a text box (XSLFTextShape)
                if (shape instanceof org.apache.poi.xslf.usermodel.XSLFTextShape) {
                    org.apache.poi.xslf.usermodel.XSLFTextShape textShape = (org.apache.poi.xslf.usermodel.XSLFTextShape) shape;
                    // Append the text from this text shape to the text string
                    text.append(textShape.getText()).append("\n");
                }
            }
        }
        ppt.close();
        return text.toString();
    }


    private static String extractTextFromExcel(File file) throws Exception {
        Workbook wb;
        if (file.getName().endsWith("xlsx")) {
            wb = new XSSFWorkbook(new FileInputStream(file));
        } else {
            wb = new HSSFWorkbook(new FileInputStream(file));
        }

        StringBuilder text = new StringBuilder();
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                text.append(cell.toString()).append(" ");  // Replace tab with space
            }
            text.append("\n");
        }
        wb.close();
        return text.toString();
    }


    private static String[] wrapTextToFitWidth(String text, PDPageContentStream contentStream, float maxWidth) throws IOException {
        // Split the text by lines and wrap them based on the width
        String[] lines = text.split("\n");
        StringBuilder wrappedText = new StringBuilder();

        for (String line : lines) {
            String[] words = line.split(" ");
            StringBuilder currentLine = new StringBuilder();
            float currentWidth = 0;

            for (String word : words) {
                float wordWidth = PDType1Font.HELVETICA.getStringWidth(word + " ") / 1000 * 12; // width of the word in points

                if (currentWidth + wordWidth <= maxWidth) {
                    currentLine.append(word).append(" ");
                    currentWidth += wordWidth;
                } else {
                    wrappedText.append(currentLine.toString().trim()).append("\n");
                    currentLine = new StringBuilder(word + " ");
                    currentWidth = wordWidth;
                }
            }

            if (currentLine.length() > 0) {
                wrappedText.append(currentLine.toString().trim());
            }
            wrappedText.append("\n");
        }
        wrappedText.append("\n");
        return wrappedText.toString().split("\n");
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public static void main(String[] args) {
        try {
            String inputFilePath = "C:\\Users\\srinu8963\\Downloads\\Your big idea.pptx";  // Example: .docx, .pptx, .xls, .txt, etc.
            String outputPdfPath = convertToPdf(inputFilePath);
            System.out.println("PDF file created: " + outputPdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
