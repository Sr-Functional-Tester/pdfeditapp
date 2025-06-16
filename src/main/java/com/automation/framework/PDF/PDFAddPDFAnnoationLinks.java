package com.automation.framework.PDF;

import java.io.File;
import java.io.IOException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

public class PDFAddPDFAnnoationLinks {
    public static void addAnnotationLinks(String inputString) throws Exception {
    	String str[] = inputString.split(",");
    	String sourcePdfPath =  str[0].trim();
    	String annotationLinks = str[1].trim();
    	// Check if the source file exists
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

        // Open the PDF to read and write
        PdfReader reader = new PdfReader(sourcePdfPath);
        PdfWriter writer = new PdfWriter(destPdfPath);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        // Create a Document object
        Document document = new Document(pdfDocument);

        // Define the link annotation position (adjusted to be at the bottom)
        float x = 40; // X position of the link
        float y = 20; // Y position near the bottom of the page
        float width = 200; // Width of the link
        float height = 20; // Height of the link

        // Create a Rectangle for the link annotation
        Rectangle rect = new Rectangle(x, y, width, height);
        PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(rect);

        // Set the action for the annotation (this makes it a link to a URL)
        PdfAction action = PdfAction.createURI(annotationLinks);
        linkAnnotation.setAction(action);

        Link link1 = new Link(annotationLinks, linkAnnotation);
        Paragraph paragraph1 = new Paragraph();
        paragraph1.add(link1.setUnderline());

       
        PdfPage firstPage = pdfDocument.getPage(1);  // Get the first page
        Rectangle pageSize =   firstPage.getPageSize();  // Get the page size
        PageSize pageSizeObj = new PageSize(pageSize);
        System.out.println(pageSizeObj);
        // Add a new page with the same size as the first page
        pdfDocument.addNewPage(1, pageSizeObj);
        document.add(paragraph1);
        int numberOfPages = pdfDocument.getNumberOfPages();
        pdfDocument.movePage(1, numberOfPages+1); 
        
        document.close();
        
       
        //System.out.println("Annotation added successfully at the bottom of the page");
    }
}
