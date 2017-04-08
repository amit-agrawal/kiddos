package com.hmi.kiddos.util;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.Program;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

/*import com.itextpdf.layout.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
*/
@Service
public class DocumentGenerator {


	public void generateInvoice(Payment payment) {
		PdfWriter writer = null;
		try {
			writer = new PdfWriter("./" + payment.getChild().getFirstName() + "_" + payment.getTransactionNumber()
					+ System.currentTimeMillis() + ".pdf");
			PdfDocument pdf = new PdfDocument(writer);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new InvoiceBackgroundHandler());
			Document doc = new Document(pdf);
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

			doc.add(new Paragraph("INVOICE").setFont(bold).setTextAlignment(TextAlignment.CENTER));
			doc.add(new Paragraph("Happy Minds International Education LLP").setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
			doc.add(new Paragraph("201, Jayshree Plaza Near Dreams Mall L.B.S. Marg").setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
			doc.add(new Paragraph("Bhandup (W) Mumbai – 78").setFont(bold).setTextAlignment(TextAlignment.LEFT));
			doc.add(new Paragraph("Phone: 022 4123 6803, 022 6710 0512").setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
			doc.add(new Paragraph("Email: bhandup@thehappyminds.com").setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));

			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			String dateString = format.format(payment.getPaymentDate().getTime());

			doc.add(new Paragraph(dateString).setFont(bold).setTextAlignment(TextAlignment.RIGHT));

			if (payment.getChild() != null) {
				String childFullName = payment.getChild().getFirstName() + " " + payment.getChild().getLastName();
				doc.add(new Paragraph(" "));
				doc.add(new Paragraph(" "));
				doc.add(new Paragraph("Child Name: " + childFullName).setFont(bold)
						.setTextAlignment(TextAlignment.LEFT));
				doc.add(new Paragraph(" "));
			}

			
			Table table = new Table(new float[] { 4, 2, 3, 3 });
			table.setWidthPercent(100);

			process(table, "Programs", bold, true);
			process(table, "Payment Mode", bold, true);
			process(table, "Transaction/Cheque No.", bold, true);
			process(table, "Amount", bold, true);


			// Create a List
			List list = new List().setSymbolIndent(12).setListSymbol("\u2022").setFont(font);

			for (Program program : payment.getPrograms()) {
				list.add(new ListItem(program.getName()));
			}

			table.addCell(list);

			process(table, payment.getPaymentMedium().toString(), font, false);
			process(table, payment.getTransactionNumber() == null ? " " : payment.getTransactionNumber(), font, false);
			table.addCell(new Cell().add(new Paragraph(payment.getAmount().toString()).setFont(font))
					.setTextAlignment(TextAlignment.RIGHT));

			doc.add(table);

			doc.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void process(Table table, String cellData, PdfFont font, boolean isHeader) {
		if (isHeader) {
			table.addHeaderCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		} else {
			table.addCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		}
	}

}
