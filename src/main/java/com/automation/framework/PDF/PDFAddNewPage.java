package com.automation.framework.PDF;

import com.itextpdf.kernel.pdf.PdfDocument;

public class PDFAddNewPage {
	
	public static void addPdfPage(int pageNo, PdfDocument pdfDocument) {
        if(pageNo != 0)
        	pdfDocument.addNewPage(pageNo);
        else 
        	pdfDocument.addNewPage();
    }

}
