package com.automation.framework.PDF;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.text.DocumentException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PDFAddPageFromDocx {
    
    // Method to read content from DOCX file and add it to the PDF document
    public static void addPageFromDocx(String docxFilePath, PdfDocument pdfDocument) throws IOException, DocumentException {
        
        // Extract text from the DOCX file
        String docxText = extractTextFromDocx(docxFilePath);

        // Add a new page to the PDF document
       // pdfDocument.addNewPage(PageSize.A4);  // You can specify other page sizes as needed

        // Create a new document to add text (we will use iText's layout features)
        Document document = new Document(pdfDocument);

        // Add the extracted text as a paragraph to the new page
       // document.add(new Paragraph(docxText));
        
        PdfPage firstPage = pdfDocument.getPage(1);  // Get the first page
        Rectangle pageSize =   firstPage.getPageSize();  // Get the page size
        PageSize pageSizeObj = new PageSize(pageSize);
        System.out.println(pageSizeObj);
        // Add a new page with the same size as the first page
        pdfDocument.addNewPage(1, pageSizeObj);
        document.add(new Paragraph(docxText));
        int numberOfPages = pdfDocument.getNumberOfPages();
        pdfDocument.movePage(1, numberOfPages+1); 

        // Close the document (this finalizes the changes to the PDF)
        document.close();
    }

    // Helper method to extract text from a DOCX file
    public static String extractTextFromDocx(String docxFilePath) throws IOException {
        // Open the DOCX file
        FileInputStream fis = new FileInputStream(docxFilePath);
        XWPFDocument document = new XWPFDocument(fis);

        // Extract all text from paragraphs
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        StringBuilder text = new StringBuilder();

        for (XWPFParagraph paragraph : paragraphs) {
            text.append(paragraph.getText()).append("\n");
        }

        document.close();
        return text.toString();
    }

    public static void main(String[] args) throws Exception {
        String sourcePdfPath = "C:\\Users\\srinu13587\\Documents\\225669.pdf";
        String destPdfPath = "C:\\Users\\srinu13587\\Documents\\modified_225669.pdf";
        String docxFilePath = "C:\\Users\\srinu13587\\Documents\\sample.docx"; // Path to the DOCX file

        // Open the existing PDF document to read and write
        PdfReader reader = new PdfReader(sourcePdfPath);
        PdfWriter writer = new PdfWriter(destPdfPath);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        // Add a new page from the DOCX content
        addPageFromDocx(docxFilePath, pdfDocument);

        // Close the PDF document
        pdfDocument.close();

        System.out.println("New page added with content from DOCX file.");
    }
}
