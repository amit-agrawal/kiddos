package com.hmi.kiddos.model;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;

@RooDataOnDemand(entity = Program.class)
public class ProgramDataOnDemand {
	@Test
	public void toStringTestPS() {
		Program program = new Program();
		program.setType(ProgramTypes.PLAY_GROUP.toString());
		program.setBatch(Batch.MORNING_A.toString());
		program.setCenter(Centers.Bhandup);
		program.setFees(1000);
		program.setTerm(PreSchoolTerms.Term_1_2016_17.toString());
		
		assertEquals("PLAY_GROUP : Term_1_2016_17 : MORNING_A", program + "");
	}

	@Test
	public void toStringTestDC() {
		Program program = new Program();
		program.setType(ProgramTypes.DAY_CARE.toString());
		program.setBatch(ProgramSubTypes.INFANT_EVENING_HALF_DAY_DC.toString());
		program.setCenter(Centers.Bhandup);
		program.setFees(1000);
		program.setTerm("2016 May");
		
		assertEquals("DAY_CARE : 2016 May : INFANT_EVENING_HALF_DAY_DC", program.toString());
	}

	@Test
	public void isPreschoolTest() {

		Program program = new Program();
		program.setType(ProgramTypes.PLAY_GROUP.toString());

		assertEquals(true, program.isPreSchool());
	}
}
