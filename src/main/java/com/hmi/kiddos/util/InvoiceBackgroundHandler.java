package com.hmi.kiddos.util;

import java.io.IOException;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;

public class InvoiceBackgroundHandler implements IEventHandler {

	@Override
	public void handleEvent(Event event) {
		PdfFont font;
		try {
			font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			int pageNumber = pdfDoc.getPageNumber(page);
			Rectangle pageSize = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

			// Set background
			/*
			 * Color limeColor = new DeviceCmyk(0.208f, 0, 0.584f, 0); Color
			 * blueColor = new DeviceCmyk(0.445f, 0.0546f, 0, 0.0667f);
			 * pdfCanvas.saveState().setFillColor(pageNumber % 2 == 1 ?
			 * limeColor : blueColor) .rectangle(pageSize.getLeft(),
			 * pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight())
			 * .fill().restoreState();
			 */

			// Add header and footer
			/*
			 * pdfCanvas.beginText().setFontAndSize(font,
			 * 9).moveText(pageSize.getWidth() / 2 - 60, pageSize.getTop() - 20)
			 * .showText("Happy Minds Internal Education LLP").moveText(60,
			 * -pageSize.getTop() + 30)
			 * .showText(String.valueOf(pageNumber)).endText();
			 */ // Add watermark
			Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
			/*
			 * canvas.setProperty(Property.FONT_COLOR, Color.WHITE);
			 * canvas.setProperty(Property.FONT_SIZE, 60);
			 * canvas.setProperty(Property.FONT, bold);
			 * canvas.showTextAligned(new Paragraph("CONFIDENTIAL"), 298, 421,
			 * pdfDoc.getPageNumber(page), TextAlignment.CENTER,
			 * VerticalAlignment.MIDDLE, 45);
			 */
			PdfExtGState state = new PdfExtGState();
			state.setFillOpacity(0.3f);
			pdfCanvas.saveState().setExtGState(state);

			/*
			 * Rectangle pagesize; float x, y; pagesize =
			 * pdfPage.getPageSizeWithRotation();
			 * pdfPage.setIgnorePageRotationForContent(true);
			 * 
			 * x = (pagesize.getLeft() + pagesize.getRight()) / 2; y =
			 * (pagesize.getTop() + pagesize.getBottom()) / 2;
			 */
			ImageData logo = ImageDataFactory.create("./logoWithReg.png");
			// .scale(1.8f, 1.8f)
			// .setHorizontalAlignment(HorizontalAlignment.CENTER).setTextAlignment(TextAlignment.CENTER);

			pdfCanvas.addImage(logo, pageSize.getWidth() * 0.1f, pageSize.getWidth() * 0.3f, pageSize.getWidth() * 0.8f, false);

			pdfCanvas.restoreState();

			pdfCanvas.release();

			// doc.add(logo).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
