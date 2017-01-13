package com.hmi.kiddos.batch;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hmi.kiddos.dao.AdmissionDao;
import com.hmi.kiddos.dao.ChildDao;
import com.hmi.kiddos.dao.ProgramDao;
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Centers;
import com.hmi.kiddos.model.Child;
import com.hmi.kiddos.model.Program;

@Service
public class AdmissionsCarryForward {
	@Autowired
	private ChildDao childDao;

	@Autowired
	private ProgramDao programDao;

	@Autowired
	private AdmissionDao admissionDao;

	public void carryForward(String fromTerm, String toTerm) {
		List<Admission> admissions = admissionDao.findAllAdmissions();
		List<Program> programs = programDao.findAllPrograms();

		for (Admission fromAdmission : admissions) {
			Program fromProgram = fromAdmission.getProgram();
			if (fromProgram.getTerm().equals(fromTerm)) {
				
				Program toProgram = getToTermProgram(fromProgram, toTerm, programs);
				Child child = fromAdmission.getChild();

				Admission toAdmission = new Admission();
				toAdmission.setChild(child);
				toAdmission.setProgram(toProgram);

				toAdmission.setAdmissionDate(Calendar.getInstance());
				System.out.println("Trying to persist: " + toAdmission);
				toAdmission.persist();
				
				System.out.println("Persisted: " + toAdmission);
			}
		}
	}


	public void createNextProgramSet(String fromTerm, String toTerm, Calendar startDate, Calendar endDate) {
		List<Program> programs = programDao.findAllPrograms();

		for (Program fromProgram : programs) {
			if (fromProgram.getTerm().equals(fromTerm)) {
				
				Program toProgram = new Program();
				
				toProgram.setBatch(fromProgram.getBatch());
				toProgram.setCenter(fromProgram.getCenter());
				toProgram.setStartDate(startDate);
				toProgram.setDueDate(endDate);
				toProgram.setNotes(fromProgram.getNotes());
				toProgram.setTerm(toTerm);
				toProgram.setType(fromProgram.getType());
				toProgram.setTeacher(fromProgram.getTeacher());
				toProgram.setTeacherTwo(fromProgram.getTeacherTwo());
				toProgram.setFees(fromProgram.getFees());
				toProgram.setIsCharge(fromProgram.getIsCharge());
				System.out.println("Trying to persist: " + toProgram);
				toProgram.persist();
				
				System.out.println("Persisted: " + toProgram);
			}
		}
	}


	private Program getToTermProgram(Program fromProgram, String toTerm, List<Program> programs) {
		String batch = fromProgram.getBatch();
		String type = fromProgram.getType();
		Centers center = fromProgram.getCenter();

		Program toProgram = null;

		for (Program program : programs) {
			if (batch != null && batch.equals(program.getBatch())) {
				if (type != null && type.equals(program.getType())) {
					if (center != null && center.equals(program.getCenter()))
						if (toTerm != null && toTerm.equals(program.getTerm())) {
							toProgram = program;
						}
				}
			}
		}

		if (toProgram == null) {
			throw new IllegalStateException(
					"No future entry for from Program: " + fromProgram + " and term: " + toTerm);
		}
		return toProgram;
	}

}
