package com.hmi.kiddos.batch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Configurable
public class AdmissionsCarryForwardTest {
	@Autowired
	private AdmissionsCarryForward carryForward;

	@Test
	@Ignore
	public void carryForwardTest() {
		String fromTerm = "October";
		String toTerm = "November";
		carryForward.carryForward(fromTerm, toTerm);
	}

	@Test
	//@Ignore
	public void createNewProgramSetTest() {
		try {
			String fromTerm = "November 17";
			String toTerm = "December 17";
			Calendar startDate = new GregorianCalendar(2017, 11, 1);
			Calendar endDate = new GregorianCalendar(2017, 11, 31);
			carryForward.createNextProgramSet(fromTerm, toTerm, startDate, endDate);

		
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@Test
	@Ignore
	public void createNewProgramSetMonthlyTest() {
		try {
			String fromTerm = "Week 4, Summer 16";
			String toTerm = "Week 4, Summer 17";
			Calendar startDate = new GregorianCalendar(2017, 4, 1);
			Calendar endDate = new GregorianCalendar(2017, 4, 28);
			carryForward.createNextProgramSet(fromTerm, toTerm, startDate, endDate);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
