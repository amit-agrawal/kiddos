package com.hmi.kiddos.model;

import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.hmi.kiddos.dao.ProgramDao;
import com.hmi.kiddos.model.enums.Batch;
import com.hmi.kiddos.model.enums.Centers;
import com.hmi.kiddos.model.enums.ProgramTypes;

@Component
@Configurable
public class ProgramDataOnDemand {
	@Autowired
	private ProgramDao programDao;

	@Test
	public void toStringTestPS() {
		Program program = new Program();
		//program.setProgramType("P");
		program.setProgramTypes(ProgramTypes.PG);
		program.setBatch(Batch.MORNING_A.toString());
		program.setCenter(Centers.Bhandup);
		program.setFees(1000);
		program.setTerm("Term_1_2016_17");

		assertEquals("PLAY_GROUP : Term_1_2016_17 : MORNING_A", program + "");
	}

	@Test
	public void toStringTestDC() {
		Program program = new Program();
		program.setProgramTypes(ProgramTypes.DC);
		program.setBatch("DAY_CARE : 2016 May : INFANT_EVENING_HALF_DAY_DC");
		program.setCenter(Centers.Bhandup);
		program.setFees(1000);
		program.setTerm("2016 May");

		assertEquals("DC : 2016 May : INFANT_EVENING_HALF_DAY_DC", program.toString());
	}

	@Test
	public void isPreschoolTest() {

		Program program = new Program();
		//program.setProgramType("P");
		program.setProgramTypes(ProgramTypes.NURSERY);

		assertEquals(true, program.isPreSchool());
	}

	private Random rnd = new SecureRandom();

	private List<Program> data;

	@Autowired
	StaffDataOnDemand staffDataOnDemand;

	public Program getNewTransientProgram(int index) {
		Program obj = new Program();
		setBatch(obj, index);
		setCenter(obj, index);
		setDueDate(obj, index);
		setFees(obj, index);
		setNotes(obj, index);
		setTerm(obj, index);
		return obj;
	}

	public void setBatch(Program obj, int index) {
		String batch = "Undecided_" + index;
		obj.setBatch(batch);
	}

	public void setCenter(Program obj, int index) {
		Centers center = Centers.class.getEnumConstants()[0];
		obj.setCenter(center);
	}

	public void setDueDate(Program obj, int index) {
		Calendar dueDate = Calendar.getInstance();
		obj.setDueDate(dueDate);
	}

	public void setFees(Program obj, int index) {
		Integer fees = new Integer(index);
		if (fees < 1 || fees > 999999) {
			fees = 999999;
		}
		obj.setFees(fees);
	}

	public void setNotes(Program obj, int index) {
		String notes = "notes_" + index;
		obj.setNotes(notes);
	}

	public void setTerm(Program obj, int index) {
		String term = "term_" + index;
		obj.setTerm(term);
	}

	public void setType(Program obj, int index) {
		ProgramTypes type = ProgramTypes.class.getEnumConstants()[0];
		obj.setProgramTypes(type);
	}

	public Program getSpecificProgram(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Program obj = data.get(index);
		Long id = obj.getId();
		return programDao.findProgram(id);
	}

	public Program getRandomProgram() {
		init();
		Program obj = data.get(rnd.nextInt(data.size()));
		Long id = obj.getId();
		return programDao.findProgram(id);
	}

	public boolean modifyProgram(Program obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = programDao.findProgramEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Program' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Program>();
		for (int i = 0; i < 10; i++) {
			Program obj = getNewTransientProgram(i);
			try {
				obj.persist();
			} catch (final ConstraintViolationException e) {
				final StringBuilder msg = new StringBuilder();
				for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
					final ConstraintViolation<?> cv = iter.next();
					msg.append("[").append(cv.getRootBean().getClass().getName()).append(".")
							.append(cv.getPropertyPath()).append(": ").append(cv.getMessage())
							.append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
				}
				throw new IllegalStateException(msg.toString(), e);
			}
			obj.flush();
			data.add(obj);
		}
	}
}
