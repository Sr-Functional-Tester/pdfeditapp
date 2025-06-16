package com.automation.framework.PDF;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class ConvertPPTXtoPDF {

    // Custom PPTX slide size (in pixels)
    private static final int PPTX_WIDTH_PX = 720; // 963px width
    private static final int PPTX_HEIGHT_PX = 400; // 532px height

    public static void main(String[] args) {
        try {
            String inputFilePath = "C:\\Users\\srinu8963\\Downloads\\Yourbigidea.pptx"; // Change this to your file path
            String outputPdfPath = convertToPdf(inputFilePath);
            System.out.println("PDF file created at: " + outputPdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertToPdf(String inputFilePath) throws Exception {
        String fileExtension = getFileExtension(inputFilePath);
        String outputFile = inputFilePath.replaceFirst("[.][^.]+$", "") + ".pdf";

        // Create PDF document
        PDDocument pdfDocument = new PDDocument();

        if (fileExtension.equals("pptx")) {
            // Convert PPTX to images and add to PDF
            convertPptxToPdf(inputFilePath, pdfDocument);
        }  else {
            throw new Exception("Unsupported file format: " + fileExtension);
        }

        // Save the PDF
        pdfDocument.save(outputFile);
        pdfDocument.close();
        return outputFile;
    }

    private static void convertPptxToPdf(String inputFilePath, PDDocument pdfDocument) throws Exception {
        FileInputStream pptxFile = new FileInputStream(inputFilePath);
        XMLSlideShow ppt = new XMLSlideShow(pptxFile);

        // Render each slide to an image and add to the PDF
        for (XSLFSlide slide : ppt.getSlides()) {
            BufferedImage image = new BufferedImage(PPTX_WIDTH_PX , PPTX_HEIGHT_PX , BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            slide.draw(graphics);  // Draw slide contents onto image
            ImageIO.write(image, "png", new File("slide.png"));
            addImageToPdf(pdfDocument, "slide.png");
        }
        ppt.close();
    }

    
    private static void addImageToPdf(PDDocument pdfDocument, String imagePath) throws Exception {
        // Create PDF page with custom size (963x532)
        PDPage page = new PDPage(new org.apache.pdfbox.pdmodel.common.PDRectangle(PPTX_WIDTH_PX, PPTX_HEIGHT_PX));
        pdfDocument.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);

        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, pdfDocument);
        // Fit image into the page size
        contentStream.drawImage(pdImage, 0, 0, PPTX_WIDTH_PX, PPTX_HEIGHT_PX);
        contentStream.close();
    }

    private static String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
    }
}
