package com.hmi.kiddos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hmi.kiddos.batch.PaySlipBatch;

@RequestMapping("/payslips")
@Controller
public class PaySlipController {

	@Autowired
	private PaySlipBatch paySlipBatch;
	
	@RequestMapping(value = "/generateAndMail", method = RequestMethod.GET)
	public void generateAndMailPayslips(@PathVariable("path") String path) {
		paySlipBatch.runPaySlipBatchProcess(path, true);	
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public void generatePayslips(@PathVariable("path") String path) {
		paySlipBatch.runPaySlipBatchProcess(path, false);	
	}
}