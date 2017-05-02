package com.hmi.kiddos.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class PaymentIntegrationTest {

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	PaymentDataOnDemand dod;

	@Autowired
	ProgramDataOnDemand pdod;

	@Test
	public void testCountPayments() {
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", dod.getRandomPayment());
		long count = Payment.countPayments();
		Assert.assertTrue("Counter for 'Payment' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindPayment() {
		Payment obj = dod.getRandomPayment();
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide an identifier", id);
		obj = Payment.findPayment(id);
		Assert.assertNotNull("Find method for 'Payment' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Payment' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllPayments() {
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", dod.getRandomPayment());
		long count = Payment.countPayments();
		Assert.assertTrue(
				"Too expensive to perform a find all test for 'Payment', as there are " + count
						+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 5000);
		List<Payment> result = Payment.findAllPayments();
		Assert.assertNotNull("Find all method for 'Payment' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Payment' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindPaymentEntries() {
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", dod.getRandomPayment());
		long count = Payment.countPayments();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Payment> result = Payment.findPaymentEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'Payment' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'Payment' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		Payment obj = dod.getRandomPayment();
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide an identifier", id);
		obj = Payment.findPayment(id);
		Assert.assertNotNull("Find method for 'Payment' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyPayment(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'Payment' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		Payment obj = dod.getRandomPayment();
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide an identifier", id);
		obj = Payment.findPayment(id);
		boolean modified = dod.modifyPayment(obj);
		Integer currentVersion = obj.getVersion();
		Payment merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
				id);
		Assert.assertTrue("Version for 'Payment' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", dod.getRandomPayment());
		Payment obj = dod.getNewTransientPayment(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Payment' identifier to be null", obj.getId());
		try {
			obj.persist();
		} catch (final ConstraintViolationException e) {
			final StringBuilder msg = new StringBuilder();
			for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
				final ConstraintViolation<?> cv = iter.next();
				msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath())
						.append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue())
						.append(")").append("]");
			}
			throw new IllegalStateException(msg.toString(), e);
		}
		obj.flush();
		Assert.assertNotNull("Expected 'Payment' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		Payment obj = dod.getRandomPayment();
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide an identifier", id);
		obj = Payment.findPayment(id);
		obj.remove();
		obj.flush();
		Assert.assertNull("Failed to remove 'Payment' with identifier '" + id + "'", Payment.findPayment(id));
	}

	@Test
	public void testCreatePaymentWithPrograms() {
		// Given
		Payment obj = dod.getRandomPayment();

		Payment payment = new Payment();
		payment.setAmount(1000);
		payment.setChild(obj.getChild());
		Program program = pdod.getNewTransientProgram(0);
		program.setProgramType("D");
		Set<Program> programs = new HashSet<>();
		programs.add(program);
		payment.setDaycarePrograms(programs);
		payment.setSendMail(false);
		payment.setPaymentMedium(PaymentMedium.CASH);

		// When
		payment.persist();

		// Then
		Assert.assertNotNull("Data on demand for 'Payment' failed to initialize correctly", payment);
		Long id = payment.getId();
		Assert.assertNotNull("Data on demand for 'Payment' failed to provide an identifier", payment);

		payment = Payment.findPayment(id);
		Assert.assertTrue(payment.getDaycarePrograms().size() == 1);

		payment.remove();
		payment.flush();
		Assert.assertNull("Failed to remove 'Payment' with identifier '" + id + "'", Payment.findPayment(id));

	}
}