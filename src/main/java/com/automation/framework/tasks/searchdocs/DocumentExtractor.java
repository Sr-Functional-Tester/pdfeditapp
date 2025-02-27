package com.automation.framework.tasks.searchdocs;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.*;
import java.util.Iterator;

public class DocumentExtractor {

    // Main method to extract text based on the file type
    public static String extractTextFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        String fileExtension = getFileExtension(file);

        String extractedText = "";
        System.out.print("document extension"+fileExtension);

        // Check the file extension and extract text accordingly
        switch (fileExtension.toLowerCase()) {
            case "pdf":
                extractedText = extractTextFromPDF(file);
                break;
            case "docx":
                extractedText = extractTextFromDocx(file);
                break;
            case "pptx":
                extractedText = extractTextFromPptx(file);
                break;
            case "xls":
            case "xlsx":
                extractedText = extractTextFromExcel(file);
                break;
            case "txt":
                extractedText = extractTextFromText(file);
                break;
            default:
                throw new IOException("Unsupported file format: " + fileExtension);
        }
        return extractedText;
    }

    // Extract text from PDF
    private static String extractTextFromPDF(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        String text = new org.apache.pdfbox.text.PDFTextStripper().getText(document);
        document.close();
        return text;
    }

    // Extract text from DOCX files
    private static String extractTextFromDocx(File file) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));
        StringBuilder text = new StringBuilder();
        for (org.apache.poi.xwpf.usermodel.XWPFParagraph paragraph : document.getParagraphs()) {
            text.append(paragraph.getText()).append("\n");
        }
        document.close();
        return text.toString();
    }

    // Extract text from PPTX files
    private static String extractTextFromPptx(File file) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
        StringBuilder text = new StringBuilder();
        for (XSLFSlide slide : ppt.getSlides()) {
            for (XSLFShape shape : slide.getShapes()) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    text.append(textShape.getText()).append("\n");
                }
            }
        }
        ppt.close();
        return text.toString();
    }

    // Extract text from Excel files (both XLS and XLSX)
    private static String extractTextFromExcel(File file) throws IOException {
        Workbook wb;
                if (file.getName().endsWith("xlsx")) {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                } else {
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }

                StringBuilder text = new StringBuilder();
                Sheet sheet = wb.getSheetAt(0); // Extracting text from the first sheet

                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        text.append(cellIterator.next().toString()).append(" "); // Extract cell content
                    }
                    text.append("\n");
                }
                wb.close();
                return text.toString();
            }

            // Extract text from plain text files
            private static String extractTextFromText(File file) throws IOException {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
                br.close();
                return content.toString();
            }

            // Utility method to get the file extension
            private static String getFileExtension(File file) {
                String fileName = file.getName();
                return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            }
        }
