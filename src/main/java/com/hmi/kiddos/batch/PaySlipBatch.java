package com.hmi.kiddos.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hmi.kiddos.model.SalaryInfo;
import com.hmi.kiddos.service.CreatePaySlip;
import com.hmi.kiddos.service.MailPaySlip;

@Service
public class PaySlipBatch {

	public PaySlipBatch(MailPaySlip mailPaySlip, CreatePaySlip createPaySlip) {
		super();
		this.mailPaySlip = mailPaySlip;
		this.createPaySlip = createPaySlip;
	}

	public PaySlipBatch() {
	}

	@Autowired
	private MailPaySlip mailPaySlip;

	@Autowired
	private CreatePaySlip createPaySlip;

	public void runPaySlipBatchProcess(String filePath, boolean sendMail) {

		try {
			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			int count = 0;

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				SalaryInfo salaryInfo = processRow(currentRow);

				if (salaryInfo != null) {
					count++;
					String docPath = createPaySlip.createPaySlipDoc(salaryInfo);
					Logger.getLogger(PaySlipBatch.class).info(count + " Salary Slip Doc Path is " + docPath);

					if (sendMail) {
						if (salaryInfo.getSendMail().trim().equals("Y")) {
							mailPaySlip.mailPaySlipDocToEmployee(docPath, salaryInfo);
							Logger.getLogger(PaySlipBatch.class)
									.info(count + " Salary Slip Mailed from path " + docPath);
						} else {
							Logger.getLogger(PaySlipBatch.class)
									.warn("Not processing as sendMail flag is: " + salaryInfo.getSendMail());
						}
					}
				}

				Logger.getLogger(PaySlipBatch.class).info("Number of salary slips generated: " + count);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private SalaryInfo processRow(Row currentRow) {
		SalaryInfo salaryInfo = new SalaryInfo();
		if (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setSendMail(currentRow.getCell(3).getStringCellValue());

		if (currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setFirstName(currentRow.getCell(4).getStringCellValue());

		if (currentRow.getCell(5) != null && currentRow.getCell(5).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setEmailId(currentRow.getCell(5).getStringCellValue());

		if (salaryInfo.getEmailId() == null || !isValidEmailAddress(salaryInfo.getEmailId())) {
			Logger.getLogger(PaySlipBatch.class)
					.warn("Email address not valid, not parsing further " + currentRow.getRowNum());
			return null;
		}

		if (currentRow.getCell(6) != null && currentRow.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setGrossPay(currentRow.getCell(6).getNumericCellValue());

		if (currentRow.getCell(7) != null && currentRow.getCell(7).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setWorkingDays(currentRow.getCell(7).getNumericCellValue());

		if (currentRow.getCell(8) != null && currentRow.getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setHolidays(currentRow.getCell(8).getNumericCellValue());

		if (currentRow.getCell(9) != null && currentRow.getCell(9).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setDeductionDays(currentRow.getCell(9).getNumericCellValue());

		if (currentRow.getCell(10) != null && currentRow.getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPayAfterHolidays(currentRow.getCell(10).getNumericCellValue());

		if (currentRow.getCell(11) != null && currentRow.getCell(11).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtIncentive(currentRow.getCell(11).getNumericCellValue());

		if (currentRow.getCell(12) != null && currentRow.getCell(12).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPT(currentRow.getCell(12).getNumericCellValue());

		if (currentRow.getCell(13) != null && currentRow.getCell(13).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPfEmployeeDeduction(currentRow.getCell(13).getNumericCellValue());

		if (currentRow.getCell(15) != null && currentRow.getCell(15).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPfGovernmentContribution(currentRow.getCell(15).getNumericCellValue());

		if (currentRow.getCell(17) != null && currentRow.getCell(17).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setRemarks(currentRow.getCell(17).getStringCellValue());

		if (currentRow.getCell(18) != null && currentRow.getCell(18).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setNetPay(currentRow.getCell(18).getNumericCellValue());

		if (currentRow.getCell(19) != null && currentRow.getCell(19).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setClRemaining(currentRow.getCell(19).getNumericCellValue());

		if (currentRow.getCell(20) != null && currentRow.getCell(20).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setClConsidered(currentRow.getCell(20).getNumericCellValue());

		if (currentRow.getCell(21) != null && currentRow.getCell(21).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtherLeavesRemaining(currentRow.getCell(21).getNumericCellValue());

		if (currentRow.getCell(22) != null && currentRow.getCell(22).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtherLeavesRemaining(currentRow.getCell(22).getNumericCellValue());

		if (currentRow.getCell(23) != null && currentRow.getCell(23).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setSalaryWithheld(currentRow.getCell(23).getNumericCellValue());

		if (currentRow.getCell(24) != null && currentRow.getCell(24).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setMonth(currentRow.getCell(24).getStringCellValue());

		if (salaryInfo.getMonth() == null || salaryInfo.getMonth().isEmpty()) {
			Logger.getLogger(this.getClass()).error("Month is not set for " + salaryInfo.getFirstName());
			return null;
		}

		double netCalculatedSalary = salaryInfo.getPayAfterHolidays() - salaryInfo.getPfEmployeeDeduction()
				- salaryInfo.getPT() + salaryInfo.getOtIncentive() - salaryInfo.getSalaryWithheld();

		Logger.getLogger(this.getClass()).info(salaryInfo);

		if (netCalculatedSalary != salaryInfo.getNetPay()) {
			Logger.getLogger(this.getClass())
					.error("Issue with net salary calculation for " + salaryInfo.getFirstName());
			return null;
		}

		if (salaryInfo.getClConsidered() != salaryInfo.getHolidays() - salaryInfo.getDeductionDays()) {
			Logger.getLogger(this.getClass()).error("Issue with net CL calculation for " + salaryInfo.getFirstName());
			return null;
		}

		return salaryInfo;
	}

	public static void main(String[] args) {

		PaySlipBatch paySlipBatch = new PaySlipBatch(new MailPaySlip(), new CreatePaySlip());

		paySlipBatch.runPaySlipBatchProcess(args[0], true);

	}
}