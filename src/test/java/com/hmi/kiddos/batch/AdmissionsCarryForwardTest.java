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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Configurable
public class AdmissionsCarryForwardTest {
	@Autowired
    private AdmissionsCarryForward carryForward = new AdmissionsCarryForward();
	
	@Test
	@Ignore
	public void carryForwardTest() {
		String fromTerm = "October";
		String toTerm = "November";
		carryForward.carryForward(fromTerm, toTerm);		
	}
	

	@Test
	@Ignore
	public void createNewProgramSetTest() {
		String fromTerm = "October";
		String toTerm = "January 17";
		Calendar startDate = new GregorianCalendar(2017,1,1);
		Calendar endDate = new GregorianCalendar(2017,1,31);
		carryForward.createNextProgramSet(fromTerm, toTerm, startDate, endDate);
	}
}
