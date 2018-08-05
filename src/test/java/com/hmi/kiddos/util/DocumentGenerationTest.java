package com.hmi.kiddos.util;

import java.util.Calendar;

import org.junit.Assert;
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
public class DocumentGenerationTest {
	@Autowired
	private InvoiceDocumentGenerator docGenerator;

	@Autowired
	PaymentDataOnDemand dod;

	@Test
	public void createInvoiceTest() {
		Payment obj = dod.getRandomPayment();
		obj.setNextFeeDueAmount(19903);
		obj.setNextFeeDueDate(Calendar.getInstance());
		obj.setPayer("Ajay");
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		String docPath = docGenerator.generateInvoice(obj);

		Assert.assertNotNull(docPath);
	}
}