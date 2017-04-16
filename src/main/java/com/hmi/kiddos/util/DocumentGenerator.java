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
	private SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

	public String generateInvoice(Payment payment) {
		String path = null;

		try {
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			path = "./" + payment.getChild().getFirstName() + "_" + payment.getChild().getLastName() + "_"
					+ System.currentTimeMillis() + ".pdf";
			PdfWriter writer = new PdfWriter(path);
			PdfDocument pdf = new PdfDocument(writer);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new InvoiceBackgroundHandler());
			Document doc = new Document(pdf);

			doc.add(new Paragraph("RECEIPT").setFont(bold).setTextAlignment(TextAlignment.CENTER));
			addHeader(doc, bold);

			addDateReceiptNo(payment, doc, bold);

			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			addChildPayerInfo(payment, doc, bold);

			doc.add(new Paragraph(" "));

			addTable(payment, doc, font);

			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			addNextDueInfo(payment, doc, bold);

			doc.close();

		} catch (Exception ex) {
			Logger.getLogger(DocumentGenerator.class).error("Could not generate receipt", ex);
		}
		return path;
	}

	private SimpleDateFormat addDateReceiptNo(Payment payment, Document doc, PdfFont bold) {
		String dateString = format.format(payment.getPaymentDate().getTime());

		doc.add(new Paragraph(dateString).setFont(bold).setTextAlignment(TextAlignment.RIGHT));
		String receiptNumber = payment.getId().toString();
		doc.add(new Paragraph("Receipt Number: " + receiptNumber).setFont(bold).setTextAlignment(TextAlignment.RIGHT));
		return format;
	}

	private void addChildPayerInfo(Payment payment, Document doc, PdfFont bold) {
		if (payment.getPayer() != null) {
			doc.add(new Paragraph("Name: " + payment.getPayer()).setFont(bold).setTextAlignment(TextAlignment.LEFT));
		}
		if (payment.getChild() != null) {
			String childFullName = payment.getChild().getFirstName() + " " + payment.getChild().getLastName();
			doc.add(new Paragraph("Child Name: " + childFullName).setFont(bold).setTextAlignment(TextAlignment.LEFT));
		}
	}

	private void addNextDueInfo(Payment payment, Document doc, PdfFont bold) {
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

	private void addTable(Payment payment, Document doc, PdfFont font) {
		Table table = new Table(new float[] { 4, 2, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Programs", true, font);
		process(table, "Payment Mode", true, font);
		process(table, "Transaction/Cheque No.", true, font);
		process(table, "Amount(Rs.)", true, font);

		// Create a List
		List list = new List().setSymbolIndent(12).setListSymbol("\u2022").setFont(font);

		for (Program program : payment.getPrograms()) {
			list.add(new ListItem(program.getName()));
		}

		table.addCell(list);

		process(table, payment.getPaymentMedium().toString(), false, font);
		process(table, payment.getTransactionNumber() == null ? " " : payment.getTransactionNumber(), false, font);
		table.addCell(new Cell().add(new Paragraph(payment.getAmount().toString()).setFont(font))
				.setTextAlignment(TextAlignment.RIGHT));

		doc.add(table);
	}

	private void addHeader(Document doc, PdfFont bold) {
		doc.add(new Paragraph("Happy Minds International Education LLP").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("201, Jayshree Plaza Near Dreams Mall L.B.S. Marg").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Bhandup (W) Mumbai – 78").setFont(bold).setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Phone: 022 4123 6803, 022 6710 0512").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		doc.add(new Paragraph("Email: bhandup@thehappyminds.com").setFont(bold).setTextAlignment(TextAlignment.LEFT));
	}

	public void process(Table table, String cellData, boolean isHeader, PdfFont font) {
		if (isHeader) {
			table.addHeaderCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		} else {
			table.addCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		}
	}
}