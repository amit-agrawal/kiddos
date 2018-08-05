package com.hmi.kiddos.service;

import com.hmi.kiddos.model.SalaryInfo;
import com.hmi.kiddos.util.PaySlipDocumentGenerator;

public class CreatePaySlip {

	public String createPaySlipDoc(SalaryInfo salaryInfo) {
		PaySlipDocumentGenerator paySlipDocumentGenerator = new PaySlipDocumentGenerator();
		String docPath = paySlipDocumentGenerator.generatePaySlip(salaryInfo);
		return docPath;
	}

}
