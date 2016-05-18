package com.hmi.kiddos.model;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
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


	private Random rnd = new SecureRandom();

	private List<Child> data;

	public Child getNewTransientChild(int index) {
        Child obj = new Child();
        setAddress(obj, index);
        setAllergy(obj, index);
        setBloodGroup(obj, index);
        setDob(obj, index);
        setEmailOne(obj, index);
        setEmailThree(obj, index);
        setEmailTwo(obj, index);
        setFatherName(obj, index);
        setFatherOrganization(obj, index);
        setFirstName(obj, index);
        setGender(obj, index);
        setLastName(obj, index);
        setMiddleName(obj, index);
        setMotherName(obj, index);
        setMotherOrganization(obj, index);
        setNationality(obj, index);
        setNotes(obj, index);
        setPhoneFather(obj, index);
        setPhoneHome(obj, index);
        setPhoneMother(obj, index);
        setPincode(obj, index);
        return obj;
    }

	public void setAddress(Child obj, int index) {
        String address = "address_" + index;
        if (address.length() > 200) {
            address = address.substring(0, 200);
        }
        obj.setAddress(address);
    }

	public void setAllergy(Child obj, int index) {
        String allergy = "allergy_" + index;
        if (allergy.length() > 150) {
            allergy = allergy.substring(0, 150);
        }
        obj.setAllergy(allergy);
    }

	public void setBloodGroup(Child obj, int index) {
        String bloodGroup = "bloodGroup_" + index;
        if (bloodGroup.length() > 20) {
            bloodGroup = bloodGroup.substring(0, 20);
        }
        obj.setBloodGroup(bloodGroup);
    }

	public void setDob(Child obj, int index) {
        Calendar dob = Calendar.getInstance();
        obj.setDob(dob);
    }

	public void setEmailOne(Child obj, int index) {
        String emailOne = "foo" + index + "@bar.com";
        if (emailOne.length() > 100) {
            emailOne = emailOne.substring(0, 100);
        }
        obj.setEmailOne(emailOne);
    }

	public void setEmailThree(Child obj, int index) {
        String emailThree = "foo" + index + "@bar.com";
        if (emailThree.length() > 100) {
            emailThree = emailThree.substring(0, 100);
        }
        obj.setEmailThree(emailThree);
    }

	public void setEmailTwo(Child obj, int index) {
        String emailTwo = "foo" + index + "@bar.com";
        if (emailTwo.length() > 100) {
            emailTwo = emailTwo.substring(0, 100);
        }
        obj.setEmailTwo(emailTwo);
    }

	public void setFatherName(Child obj, int index) {
        String fatherName = "fatherName_" + index;
        if (fatherName.length() > 60) {
            fatherName = fatherName.substring(0, 60);
        }
        obj.setFatherName(fatherName);
    }

	public void setFatherOrganization(Child obj, int index) {
        String fatherOrganization = "fatherOrganization_" + index;
        if (fatherOrganization.length() > 50) {
            fatherOrganization = fatherOrganization.substring(0, 50);
        }
        obj.setFatherOrganization(fatherOrganization);
    }

	public void setFirstName(Child obj, int index) {
        String firstName = "firstName_" + index;
        if (firstName.length() > 30) {
            firstName = firstName.substring(0, 30);
        }
        obj.setFirstName(firstName);
    }

	public void setGender(Child obj, int index) {
        Gender gender = Gender.class.getEnumConstants()[0];
        obj.setGender(gender);
    }

	public void setLastName(Child obj, int index) {
        String lastName = "lastName_" + index;
        if (lastName.length() > 30) {
            lastName = lastName.substring(0, 30);
        }
        obj.setLastName(lastName);
    }

	public void setMiddleName(Child obj, int index) {
        String middleName = "middleName_" + index;
        if (middleName.length() > 30) {
            middleName = middleName.substring(0, 30);
        }
        obj.setMiddleName(middleName);
    }

	public void setMotherName(Child obj, int index) {
        String motherName = "motherName_" + index;
        if (motherName.length() > 60) {
            motherName = motherName.substring(0, 60);
        }
        obj.setMotherName(motherName);
    }

	public void setMotherOrganization(Child obj, int index) {
        String motherOrganization = "motherOrganization_" + index;
        if (motherOrganization.length() > 50) {
            motherOrganization = motherOrganization.substring(0, 50);
        }
        obj.setMotherOrganization(motherOrganization);
    }

	public void setNationality(Child obj, int index) {
        String nationality = "nationality_" + index;
        if (nationality.length() > 50) {
            nationality = nationality.substring(0, 50);
        }
        obj.setNationality(nationality);
    }

	public void setNotes(Child obj, int index) {
        String notes = "notes_" + index;
        if (notes.length() > 400) {
            notes = notes.substring(0, 400);
        }
        obj.setNotes(notes);
    }

	public void setPhoneFather(Child obj, int index) {
        String phoneFather = "phoneFather_" + index;
        if (phoneFather.length() > 30) {
            phoneFather = phoneFather.substring(0, 30);
        }
        obj.setPhoneFather(phoneFather);
    }

	public void setPhoneHome(Child obj, int index) {
        String phoneHome = "phoneHome_" + index;
        if (phoneHome.length() > 30) {
            phoneHome = phoneHome.substring(0, 30);
        }
        obj.setPhoneHome(phoneHome);
    }

	public void setPhoneMother(Child obj, int index) {
        String phoneMother = "phoneMother_" + index;
        if (phoneMother.length() > 30) {
            phoneMother = phoneMother.substring(0, 30);
        }
        obj.setPhoneMother(phoneMother);
    }

	public void setPincode(Child obj, int index) {
        String pincode = "pinc_" + index;
        if (pincode.length() > 6) {
            pincode = pincode.substring(0, 6);
        }
        obj.setPincode(pincode);
    }

	public Child getSpecificChild(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Child obj = data.get(index);
        Long id = obj.getId();
        return Child.findChild(id);
    }

	public Child getRandomChild() {
        init();
        Child obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Child.findChild(id);
    }

	public boolean modifyChild(Child obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Child.findChildEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Child' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Child>();
        for (int i = 0; i < 10; i++) {
            Child obj = getNewTransientChild(i);
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
