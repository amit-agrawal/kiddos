package com.hmi.kiddos.model;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;

@RooDataOnDemand(entity = Admission.class)
public class AdmissionDataOnDemand {


	@Test
	public void calculateFeesTest() {
		Program program = new Program();
		program.setType("Summer Camp");
		program.setBatch("Morning B1");
		program.setFees(1900);
		Program program1 = new Program();
		program1.setType("Summer Camp");
		program1.setBatch("Morning B1");
		program1.setFees(1900);
		Program program3 = new Program();
		program3.setType("Summer Camp");
		program3.setBatch("Morning B1");
		program3.setFees(1900);
		
		Set<Program> programs = new HashSet<Program>();
		programs.add(program);
		programs.add(program1);
		programs.add(program3);
		
		Child child = new Child();
		child.setFirstName("Ajay");
		child.setMiddleName("Kumar");
		child.setLastName("Sharma");

		Admission admission = new Admission();
		admission.setChild(child);
		admission.setPrograms(programs);
		assertEquals(5800, (int) admission.getFeesExpected());
	}

	
	
	public static void main(String[] args) {
	}
}
