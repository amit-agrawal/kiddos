package com.hmi.kiddos.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

public class InvoiceBackgroundHandler implements IEventHandler {

	@Override
	public void handleEvent(Event event) {
		try {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			Rectangle pageSize = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

			PdfExtGState state = new PdfExtGState();
			state.setFillOpacity(0.3f);
			pdfCanvas.saveState().setExtGState(state);

			Resource resource = new ClassPathResource("logoWithReg.png");

			ImageData logo = ImageDataFactory.create(resource.getURL());

			pdfCanvas.addImage(logo, pageSize.getWidth() * 0.1f, pageSize.getWidth() * 0.3f, pageSize.getWidth() * 0.8f,
					false);

			pdfCanvas.restoreState();

			pdfCanvas.release();

		} catch (IOException e) {
			Logger.getLogger(DocumentGenerator.class).error("Could not set background of invoice", e);
		}
	}
}
