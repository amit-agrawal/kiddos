package com.hmi.kiddos.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Scanner;

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

	public static final String PAYSLIP_PATH = "/payslips/docs";
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

	public void runPaySlipBatchProcess(Workbook workbook, boolean sendMail) {

		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		int count = 0;
		int mailedCount = 0;

/*		Path sourcePath      = Paths.get("/docs/");
		Path destinationPath = Paths.get("/oldPlaySlips/");

		try {
		    Files.move(sourcePath, destinationPath,
		            StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
		    //moving file failed.
		    e.printStackTrace();
		}
*/		
		while (iterator.hasNext()) {

			Row currentRow = iterator.next();
			SalaryInfo salaryInfo = processRow(currentRow);

			if (salaryInfo != null) {
				count++;
				String docPath = createPaySlip.createPaySlipDoc(salaryInfo);
				// Logger.getLogger(PaySlipBatch.class).info(count + " Salary
				// Slip Doc Path is " + docPath);

				if (sendMail) {
					if (salaryInfo.getSendMail().trim().equals("Y")) {
						mailPaySlip.mailPaySlipDocToEmployee(docPath, salaryInfo);
						Logger.getLogger(PaySlipBatch.class).info(count + " Salary Slip Mailed from path " + docPath);
						mailedCount++;
					}
				}
			}

		}
		Logger.getLogger(PaySlipBatch.class).warn("Number of salary slips generated at " + PaySlipBatch.PAYSLIP_PATH + " : " + count);
		Logger.getLogger(PaySlipBatch.class).warn("Number of salary slips mailed: " + mailedCount);

	}

	private static Workbook extractExcelWorkBook(String filePath) throws FileNotFoundException, IOException {
		FileInputStream excelFile = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(excelFile);
		return workbook;
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

		if (salaryInfo.getSendMail() == null
				|| !(salaryInfo.getSendMail().equals("N") || salaryInfo.getSendMail().equals("Y"))) {
			// Logger.getLogger(PaySlipBatch.class)
			// .info("Send Mail flag not N or Y, not parsing the row further " +
			// currentRow.getRowNum());
			return null;
		}

		if (currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setFirstName(currentRow.getCell(4).getStringCellValue());

		if (currentRow.getCell(5) != null && currentRow.getCell(5).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setEmailId(currentRow.getCell(5).getStringCellValue());

		if (salaryInfo.getEmailId() == null || !isValidEmailAddress(salaryInfo.getEmailId())) {
			Logger.getLogger(PaySlipBatch.class)
					.error("Email address not valid, not parsing further " + currentRow.getRowNum());
			return null;
		}

		if (currentRow.getCell(6) != null && currentRow.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setGrossPay(currentRow.getCell(6).getNumericCellValue());

		int counter = 13;
		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setWorkingDays(currentRow.getCell(counter).getNumericCellValue());

		counter++;
		
		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setHolidays(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setDeductionDays(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPayAfterHolidays(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtIncentive(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPT(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPfEmployeeDeduction(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		counter++; // ignore one column
		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setPfGovernmentContribution(currentRow.getCell(counter).getNumericCellValue());

		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_STRING)
			salaryInfo.setRemarks(currentRow.getCell(counter).getStringCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setNetPay(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setClRemaining(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setClConsidered(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtherLeavesRemaining(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setOtherLeavesConsumed(currentRow.getCell(counter).getNumericCellValue());
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setSalaryWithheld(currentRow.getCell(counter).getNumericCellValue());

		counter++;
		if (currentRow.getCell(counter) != null) { 
				if (currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_STRING) 
					salaryInfo.setMonth(currentRow.getCell(counter).getStringCellValue());
				else if (currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
					salaryInfo.setMonth(Double.toString(currentRow.getCell(counter).getNumericCellValue()));
				else {
					Logger.getLogger(this.getClass()).error("Month is not set correctly for " + salaryInfo.getFirstName() + " " + salaryInfo);
					return null;
				}
		}
		else {
			Logger.getLogger(this.getClass()).error("Month is not set for " + salaryInfo.getFirstName() + " " + salaryInfo);
			return null;
		}
		counter++;

		if (currentRow.getCell(counter) != null && currentRow.getCell(counter).getCellType() == Cell.CELL_TYPE_NUMERIC)
			salaryInfo.setYear((int) currentRow.getCell(counter).getNumericCellValue());

		double netCalculatedSalary = salaryInfo.getPayAfterHolidays() - salaryInfo.getPfEmployeeDeduction()
				- salaryInfo.getPT() + salaryInfo.getOtIncentive() - salaryInfo.getSalaryWithheld();

		Logger.getLogger(this.getClass()).info(salaryInfo);


		if (netCalculatedSalary > 70000) {
			Logger.getLogger(this.getClass())
			.warn("Please verify high net salary of " + netCalculatedSalary + " for : " + salaryInfo.getFirstName() + " : " + salaryInfo);			
		}

		if (netCalculatedSalary < 10) {
			Logger.getLogger(this.getClass())
			.warn("Please verify very low net salary of " + netCalculatedSalary + " for : " + salaryInfo.getFirstName() + " : " + salaryInfo);			
		}

		if (salaryInfo.getPayAfterHolidays() > 10000 && salaryInfo.getPT() < 1) {
			Logger.getLogger(this.getClass())
			.warn("Please verify zero PT for : " + salaryInfo.getFirstName() + " : " + salaryInfo);			
		}

		if (salaryInfo.getGrossPay() > 15000 && salaryInfo.getPfEmployeeDeduction() > 0) {
			Logger.getLogger(this.getClass())
			.warn("Please verify non zero PF for : " + salaryInfo.getFirstName() + " : " + salaryInfo);			
		}

		double netSalaryDiff = netCalculatedSalary - salaryInfo.getNetPay();
		if (netSalaryDiff > 1 || netSalaryDiff < -1) {
			Logger.getLogger(this.getClass())
					.error("Issue with net salary calculation for " + salaryInfo.getFirstName() + " Expected Net Pay: "
							+ netCalculatedSalary + " Actual Net Pay in Sheet: " + salaryInfo.getNetPay());
			return null;
		}

		if ((salaryInfo.getClConsidered() + salaryInfo.getOtherLeavesConsumed()) != salaryInfo.getHolidays()
				- salaryInfo.getDeductionDays()) {
			Logger.getLogger(this.getClass()).error("Issue with net CL calculation for " + salaryInfo.getFirstName()
					+ " Expected vacation Considered: " + (salaryInfo.getHolidays() - salaryInfo.getDeductionDays())
					+ " Actual vacation Considered: "
					+ (salaryInfo.getClConsidered() + salaryInfo.getOtherLeavesConsumed()));
			return null;
		}

		return salaryInfo;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("What is the location of input file: ");
		String filePath = in.nextLine();
		System.out.println("Location of input file is: " + filePath);
		boolean validFile = true;
		Workbook workbook = null;
		try {
			workbook = extractExcelWorkBook(filePath);
		} catch (IOException e) {
			validFile = false;
			System.out.println("Invalid input xml file: " + filePath);
		}
		
		if (validFile) {
			System.out.println("Do you want to send mail? y/n");
			String sendMailString = in.nextLine();
			boolean sendMail = false;
			if (sendMailString != null & (sendMailString.equalsIgnoreCase("Y") || sendMailString.equalsIgnoreCase("yes")
					|| sendMailString.equalsIgnoreCase("true")))
				sendMail = true;
			System.out.println("Mails will be sent: " + sendMail);

			PaySlipBatch paySlipBatch = new PaySlipBatch(new MailPaySlip(), new CreatePaySlip());
			/*
			 * if (args.length > 1) sendMail = Boolean.parseBoolean(args[1]);
			 */
			paySlipBatch.runPaySlipBatchProcess(workbook, sendMail);
		}
	}
}