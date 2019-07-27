package com.hmi.kiddos.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.hmi.kiddos.model.SalaryInfo;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class PaySlipDocumentGenerator {
	private SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

	@Value("${address.first:First}")
	private String addressFirst;

	@Value("${address.second:Second}")
	private String addressSecond;

	@Value("${address.third:Third}")
	private String addressThird;

	@Value("${address.fourth:Fourth}")
	private String addressFourth;

	public String generatePaySlip(SalaryInfo salaryInfo) {
		String path = null;

		try {
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			path = "/docs/" + salaryInfo.getFirstName() + "_" + salaryInfo.getMonth() + "_" + System.currentTimeMillis()
					+ ".pdf";

			path = path.replace(' ', '_');
			PdfWriter writer = new PdfWriter(path);
			PdfDocument pdf = new PdfDocument(writer);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE,
					new PdfDocumentBackgroundHandler("Note: This document is confidential."));
			Document doc = new Document(pdf);

			doc.add(new Paragraph("Salary Slip").setFont(bold).setTextAlignment(TextAlignment.CENTER));
			addHeader(doc, bold);

			addDate(doc, bold);

			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(salaryInfo.getMonth()).setFont(bold).setTextAlignment(TextAlignment.CENTER));
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			addNameInfo(salaryInfo, doc, bold);

			doc.add(new Paragraph(" "));

			addFirstTable(salaryInfo, doc, font);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph("Leaves"));
			addLeavesTable(salaryInfo, doc, font);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph("Deductions"));
			addDeductionsTable(salaryInfo, doc, font);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph("Deposits"));
			addDepositsTable(salaryInfo, doc, font);

			doc.add(new Paragraph(" "));

			if (salaryInfo.getRemarks() != null && !salaryInfo.getRemarks().isEmpty())
				doc.add(new Paragraph("Note: " + salaryInfo.getRemarks()));

			doc.close();

		} catch (Exception ex) {
			Logger.getLogger(PaySlipDocumentGenerator.class).error("Could not generate payslip", ex);
			return null;
		}
		return path;
	}

	private SimpleDateFormat addDate(Document doc, PdfFont bold) {
		String dateString = format.format(Calendar.getInstance().getTime());

		doc.add(new Paragraph(dateString).setFont(bold).setTextAlignment(TextAlignment.RIGHT));
		return format;
	}

	private void addNameInfo(SalaryInfo salaryInfo, Document doc, PdfFont bold) {
		if (salaryInfo.getFirstName() != null) {
			doc.add(new Paragraph("Name: " + salaryInfo.getFirstName()).setFont(bold)
					.setTextAlignment(TextAlignment.LEFT));
		}
	}

	private void addFirstTable(SalaryInfo salaryInfo, Document doc, PdfFont font) {
		Table table = new Table(new float[] { 3, 3, 3, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Gross CTC", true, font);
		process(table, "Payable Days", true, font);
		if (salaryInfo.getOtIncentive() > 0)
			process(table, "Overtime/Incentive/Reimbursement/Dues", true, font);
		if (salaryInfo.getPfGovernmentContribution() > 0)
			process(table, "PMRPY PF", true, font);

		process(table, ((Double) salaryInfo.getGrossPay()).toString(), false, font);
		process(table, ((Double) salaryInfo.getWorkingDays()).toString(), false, font);
		if (salaryInfo.getOtIncentive() > 0)
			process(table, ((Double) salaryInfo.getOtIncentive()).toString(), false, font);
		if (salaryInfo.getPfGovernmentContribution() > 0)
			process(table, ((Double) salaryInfo.getPfGovernmentContribution()).toString(), false, font);

		doc.add(table);
	}

	private void addDeductionsTable(SalaryInfo salaryInfo, Document doc, PdfFont font) {
		Table table = new Table(new float[] { 3, 3, 3, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Holidays deduction", true, font);
		process(table, "Professional Tax", true, font);
		if (salaryInfo.getPfEmployeeDeduction() > 0)
			process(table, "PF", true, font);
		if (salaryInfo.getSalaryWithheld() > 0)
			process(table, "Deposit", true, font);

		process(table, ((Double) (salaryInfo.getGrossPay() - salaryInfo.getPayAfterHolidays())).toString(), false,
				font);
		process(table, ((Double) salaryInfo.getPT()).toString(), false, font);
		if (salaryInfo.getPfEmployeeDeduction() > 0)
			process(table, ((Double) salaryInfo.getPfEmployeeDeduction()).toString(), false, font);
		if (salaryInfo.getSalaryWithheld() > 0)
			process(table, ((Double) salaryInfo.getSalaryWithheld()).toString(), false, font);

		doc.add(table);
	}

	private void addDepositsTable(SalaryInfo salaryInfo, Document doc, PdfFont font) {
		Table table = new Table(new float[] { 3, 3, 3, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Net Salary", true, font);
		if ((salaryInfo.getPfEmployeeDeduction() + salaryInfo.getPfGovernmentContribution()) > 0)
			process(table, "Net PF", true, font);

		process(table, ((Double) salaryInfo.getNetPay()).toString(), false, font);
		if ((salaryInfo.getPfEmployeeDeduction() + salaryInfo.getPfGovernmentContribution()) > 0)
			process(table, ((Double) (salaryInfo.getPfEmployeeDeduction() + salaryInfo.getPfGovernmentContribution()))
					.toString(), false, font);

		doc.add(table);
	}

	private void addLeavesTable(SalaryInfo salaryInfo, Document doc, PdfFont font) {
		Table table = new Table(new float[] { 3, 3, 3, 3, 3, 3 });
		table.setWidthPercent(100);

		process(table, "Leaves Taken", true, font);
		process(table, "CL Consumed", true, font);
		if (salaryInfo.getOtherLeavesConsumed() > 0)
			process(table, "Other Leaves Consumed", true, font);
		process(table, "Days Deducted", true, font);
		process(table, "CL Remaining", true, font);
		if (salaryInfo.getOtherLeavesRemaining() > 0)
			process(table, "Other Leaves Remaining", true, font);

		process(table, ((Double) salaryInfo.getHolidays()).toString(), false, font);
		process(table, ((Double) salaryInfo.getClConsidered()).toString(), false, font);
		if (salaryInfo.getOtherLeavesConsumed() > 0)
			process(table, ((Double) salaryInfo.getOtherLeavesConsumed()).toString(), false, font);
		process(table, ((Double) salaryInfo.getDeductionDays()).toString(), false, font);
		process(table, ((Double) salaryInfo.getClRemaining()).toString(), false, font);
		if (salaryInfo.getOtherLeavesRemaining() > 0)
			process(table, ((Double) salaryInfo.getOtherLeavesRemaining()).toString(), false, font);

		doc.add(table);
	}

	private void addHeader(Document doc, PdfFont bold) {
		doc.add(new Paragraph("Happy Minds International Education LLP").setFont(bold)
				.setTextAlignment(TextAlignment.LEFT));
		if (addressFirst != null)
			doc.add(new Paragraph(addressFirst).setFont(bold).setTextAlignment(TextAlignment.LEFT));
		if (addressSecond != null)
			doc.add(new Paragraph(addressSecond).setFont(bold).setTextAlignment(TextAlignment.LEFT));
		if (addressThird != null)
			doc.add(new Paragraph(addressThird).setFont(bold).setTextAlignment(TextAlignment.LEFT));
		if (addressFourth != null)
			doc.add(new Paragraph(addressFourth).setFont(bold).setTextAlignment(TextAlignment.LEFT));
	}

	public void process(Table table, String cellData, boolean isHeader, PdfFont font) {
		if (isHeader) {
			table.addHeaderCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		} else {
			table.addCell(new Cell().add(new Paragraph(cellData).setFont(font)));
		}
	}

}
