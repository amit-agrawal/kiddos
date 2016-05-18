package com.hmi.kiddos.model;
import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
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

	private Random rnd = new SecureRandom();

	private List<Admission> data;

	@Autowired
    ChildDataOnDemand childDataOnDemand;

	@Autowired
    TransportationDataOnDemand transportationDataOnDemand;

	public Admission getNewTransientAdmission(int index) {
        Admission obj = new Admission();
        setAdmissionDate(obj, index);
        setChild(obj, index);
        setDiscount(obj, index);
        setJoiningDate(obj, index);
        setNotes(obj, index);
        return obj;
    }

	public void setAdmissionDate(Admission obj, int index) {
        Calendar admissionDate = Calendar.getInstance();
        obj.setAdmissionDate(admissionDate);
    }

	public void setChild(Admission obj, int index) {
        Child child = childDataOnDemand.getRandomChild();
        obj.setChild(child);
    }

	public void setDiscount(Admission obj, int index) {
        int discount = index;
        obj.setDiscount(discount);
    }

	public void setJoiningDate(Admission obj, int index) {
        Calendar joiningDate = Calendar.getInstance();
        obj.setJoiningDate(joiningDate);
    }

	public void setNotes(Admission obj, int index) {
        String notes = "notes_" + index;
        if (notes.length() > 400) {
            notes = notes.substring(0, 400);
        }
        obj.setNotes(notes);
    }

	public Admission getSpecificAdmission(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Admission obj = data.get(index);
        Long id = obj.getId();
        return Admission.findAdmission(id);
    }

	public Admission getRandomAdmission() {
        init();
        Admission obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Admission.findAdmission(id);
    }

	public boolean modifyAdmission(Admission obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Admission.findAdmissionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Admission' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Admission>();
        for (int i = 0; i < 10; i++) {
            Admission obj = getNewTransientAdmission(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
