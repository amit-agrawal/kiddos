package com.hmi.kiddos.util;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.Program;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

@Service
public class DocumentGenerator {
	private PdfFont font;
	private PdfFont bold;
	private SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

	public DocumentGenerator() {
		try {
			font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
		} catch (Exception ex) {
			Logger.getLogger(DocumentGenerator.class).error("Could not load fonts", ex);
		}
	}

	public String generateInvoice(Payment payment) {
		PdfWriter writer = null;
		Document doc = null;
		String path = null;
		try {
			path = "./" + payment.getChild().getFirstName() + "_" + payment.getChild().getLastName() + "_"
					+ System.currentTimeMillis() + ".pdf";
			writer = new PdfWriter(path);
			PdfDocument pdf = new PdfDocument(writer);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new InvoiceBackgroundHandler());
			doc = new Document(pdf);

			doc.add(new Paragraph("RECEIPT").setFont(bold).setTextAlignment(TextAlignment.CENTER));
			addHeader(doc);

			addDateReceiptNo(payment, doc);

			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			addChildPayerInfo(payment, doc);

			doc.add(new Paragraph(" "));

			addTable(payment, doc);

			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			addNextDueInfo(payment, doc);

			doc.close();

		} catch (Exception ex) {
			Logger.getLogger(DocumentGenerator.class).error("Could not generate receipt", ex);
		}
		return path;
	}

	private SimpleDateFormat addDateReceiptNo(Payment payment, Document doc) {
		String dateString = format.format(payment.getPaymentDate().getTime());

		doc.add(new Paragraph(dateString).setFont(bold).setTextAlignment(TextAlignment.RIGHT));
		String receiptNumber = payment.getId().toString();
		doc.add(new Paragraph("Receipt Number: " + receiptNumber).setFont(bold)
				.setTextAlignment(TextAlignment.RIGHT));
		return format;
	}

	private void addChildPayerInfo(Payment payment, Document doc) {
		if (payment.getPayer() != null) {
			doc.add(new Paragraph("Name: " + payment.getPayer()).setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
		}
		if (payment.getChild() != null) {
			String childFullName = payment.getChild().getFirstName() + " " + payment.getChild().getLastName();
			doc.add(new Paragraph("Child Name: " + childFullName).setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
		}
	}

	private void addNextDueInfo(Payment payment, Document doc) {
		if (payment.getNextFeeDueDate() != null) {
			String nextDueDateString = format.format(payment.getNextFeeDueDate().getTime());
			doc.add(new Paragraph("Next Fee Due Date: " + nextDueDateString).setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
		}

		if (payment.getNextFeeDueAmount() != null) {
			doc.add(new Paragraph("Next Due Fee Amount(Rs.): " + payment.getNextFeeDueAmount()).setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
		}
	}

	private void addTable(Payment payment, Document doc) {
		Table table = new Table(new float[] { 4, 2, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Programs", true);
		process(table, "Payment Mode", true);
		process(table, "Transaction/Cheque No.", true);
		process(table, "Amount(Rs.)", true);

		// Create a List
		List list = new List().setSymbolIndent(12).setListSymbol("\u2022").setFont(font);

		for (Program program : payment.getPrograms()) {
			list.add(new ListItem(program.getName()));
		}

		table.addCell(list);

		process(table, payment.getPaymentMedium().toString(), false);
		process(table, payment.getTransactionNumber() == null ? " " : payment.getTransactionNumber(), false);
		table.addCell(new Cell().add(new Paragraph(payment.getAmount().toString()).setFont(font))
				.setTextAlignment(TextAlignment.RIGHT));

		doc.add(table);
	}

	private void addHeader(Document doc) {
		doc.add(new Paragraph("Happy Minds International Education LLP").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("201, Jayshree Plaza Near Dreams Mall L.B.S. Marg").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Bhandup (W) Mumbai – 78").setFont(bold).setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Phone: 022 4123 6803, 022 6710 0512").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Email: bhandup@thehappyminds.com").setFont(bold).setTextAlignment(TextAlignment.LEFT));
	}

	public void process(Table table, String cellData, boolean isHeader) {
		if (isHeader) {
			table.addHeaderCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		} else {
			table.addCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		}
	}
}