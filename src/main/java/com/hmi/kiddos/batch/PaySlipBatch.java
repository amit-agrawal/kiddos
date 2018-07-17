package com.hmi.kiddos.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hmi.kiddos.model.SalaryInfo;
import com.hmi.kiddos.service.CreatePaySlip;
import com.hmi.kiddos.service.MailPaySlip;

public class PaySlipBatch {

	public PaySlipBatch(MailPaySlip mailPaySlip, CreatePaySlip createPaySlip) {
		super();
		this.mailPaySlip = mailPaySlip;
		this.createPaySlip = createPaySlip;
	}

	private MailPaySlip mailPaySlip;

	private CreatePaySlip createPaySlip;

	private void runPaySlipBatchProcess(String filePath) {

		try {
			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				SalaryInfo salaryInfo = fetchSalaryInfo(currentRow);

				String docPath = createPaySlip.createPaySlipDoc(salaryInfo);

				mailPaySlip.mailPaySlipDocToEmployee(docPath, salaryInfo);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private SalaryInfo fetchSalaryInfo(Row currentRow) {
		SalaryInfo salaryInfo = new SalaryInfo();
		if (currentRow.getCell(0) != null && currentRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setFirstName(currentRow.getCell(0).getStringCellValue());

		if (currentRow.getCell(1) != null && currentRow.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setLastName(currentRow.getCell(1).getStringCellValue());

		if (currentRow.getCell(2) != null && currentRow.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setBasic(currentRow.getCell(2).getNumericCellValue());

		if (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setGrossPay(currentRow.getCell(3).getNumericCellValue());

		if (currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setNetPay(currentRow.getCell(4).getNumericCellValue());

		if (currentRow.getCell(5) != null && currentRow.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPfDeduction(currentRow.getCell(4).getNumericCellValue());

		if (currentRow.getCell(6) != null && currentRow.getCell(6).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setEmailId(currentRow.getCell(6).getStringCellValue());

		if (!salaryInfo.getEmailId().contains("@"))
			throw new IllegalArgumentException("Wrong email id: " + salaryInfo.getEmailId());

		System.out.println(salaryInfo);
		return salaryInfo;
	}

	public static void main(String[] args) {

		PaySlipBatch paySlipBatch = new PaySlipBatch(new MailPaySlip(), new CreatePaySlip());

		paySlipBatch.runPaySlipBatchProcess(args[0]);

	}
}