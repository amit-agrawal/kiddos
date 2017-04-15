package com.hmi.kiddos.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.PaymentDataOnDemand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Configurable
public class MailingTest {
	@Autowired
	private MailUtil mailUtil;

	@Autowired
	private DocumentGenerator docGenerator;

	@Autowired
	PaymentDataOnDemand dod;

	@Test
	public void sendGmailTest() {
		mailUtil.sendGmail("AdmissionController", "create", "1,2", null);
	}

	@Test
	public void sendMailWithReceipt() {
		Payment obj = dod.getRandomPayment();
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		String docPath = docGenerator.generateInvoice(obj);
		mailUtil.sendGmail("PaymentController", "create", "1,2", docPath);
	}

}
