package com.hmi.kiddos.model;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;

@RooDataOnDemand(entity = Child.class)
public class ChildDataOnDemand {
	@Test
	public void toStringTest() {
		Child child = new Child();
		child.setFirstName("Ajay");
		child.setMiddleName("Kumar");
		child.setLastName("Sharma");
		child.setFatherName("Vijay Sharma");
		child.setDob(new GregorianCalendar(2013,1,28,13,24,56));

		
		assertTrue(child.toString().startsWith("Ajay Kumar Sharma, "));
	}

}
