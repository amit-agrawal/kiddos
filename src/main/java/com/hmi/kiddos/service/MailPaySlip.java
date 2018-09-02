package com.hmi.kiddos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hmi.kiddos.model.SalaryInfo;
import com.hmi.kiddos.util.MailUtil;

@Service
public class MailPaySlip {

	@Autowired
	private MailUtil mailUtil;

	
	public boolean mailPlaySlip(String docPath, String[] mailIds) {
		
		return true;
	}

	public void mailPaySlipDocToEmployee(String docPath, SalaryInfo salaryInfo) {
		mailUtil.sendPaySlip(docPath, salaryInfo);
		
	}
	
}
