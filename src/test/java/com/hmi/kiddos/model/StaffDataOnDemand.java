package com.hmi.kiddos.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.hmi.kiddos.model.enums.Centers;
import com.hmi.kiddos.model.enums.Department;
import com.hmi.kiddos.model.enums.Gender;

@Component
@Configurable
public class StaffDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Staff> data;

	public Staff getNewTransientStaff(int index) {
        Staff obj = new Staff();
        setAddress(obj, index);
        setCenter(obj, index);
        setDepartment(obj, index);
        setDob(obj, index);
        setEmailOne(obj, index);
        setEnabled(obj, index);
        setFirstName(obj, index);
        setGender(obj, index);
        setLastName(obj, index);
        setMiddleName(obj, index);
        setNotes(obj, index);
        setPassword(obj, index);
        setPhone(obj, index);
        setPhoneHome(obj, index);
        setPincode(obj, index);
        return obj;
    }

	public void setAddress(Staff obj, int index) {
        String address = "address_" + index;
        if (address.length() > 200) {
            address = address.substring(0, 200);
        }
        obj.setAddress(address);
    }

	public void setCenter(Staff obj, int index) {
        Centers center = Centers.class.getEnumConstants()[0];
        obj.setCenter(center);
    }

	public void setDepartment(Staff obj, int index) {
        Department department = Department.class.getEnumConstants()[0];
        obj.setDepartment(department);
    }

	public void setDob(Staff obj, int index) {
        Calendar dob = Calendar.getInstance();
        obj.setDob(dob);
    }

	public void setEmailOne(Staff obj, int index) {
        String emailOne = "foo" + index + "@bar.com";
        if (emailOne.length() > 30) {
            emailOne = emailOne.substring(0, 30);
        }
        obj.setEmailOne(emailOne);
    }

	public void setEnabled(Staff obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }

	public void setFirstName(Staff obj, int index) {
        String firstName = "firstName_" + index;
        if (firstName.length() > 20) {
            firstName = firstName.substring(0, 20);
        }
        obj.setFirstName(firstName);
    }

	public void setGender(Staff obj, int index) {
        Gender gender = Gender.class.getEnumConstants()[0];
        obj.setGender(gender);
    }

	public void setLastName(Staff obj, int index) {
        String lastName = "lastName_" + index;
        if (lastName.length() > 20) {
            lastName = lastName.substring(0, 20);
        }
        obj.setLastName(lastName);
    }

	public void setMiddleName(Staff obj, int index) {
        String middleName = "middleName_" + index;
        if (middleName.length() > 20) {
            middleName = middleName.substring(0, 20);
        }
        obj.setMiddleName(middleName);
    }

	public void setNotes(Staff obj, int index) {
        String notes = "notes_" + index;
        if (notes.length() > 400) {
            notes = notes.substring(0, 400);
        }
        obj.setNotes(notes);
    }

	public void setPassword(Staff obj, int index) {
        String password = "password_" + index;
        obj.setPassword(password);
    }

	public void setPhone(Staff obj, int index) {
        String phone = "phone_" + index;
        if (phone.length() > 20) {
            phone = phone.substring(0, 20);
        }
        obj.setPhone(phone);
    }

	public void setPhoneHome(Staff obj, int index) {
        String phoneHome = "phoneHome_" + index;
        if (phoneHome.length() > 20) {
            phoneHome = phoneHome.substring(0, 20);
        }
        obj.setPhoneHome(phoneHome);
    }

	public void setPincode(Staff obj, int index) {
        String pincode = "pinc_" + index;
        if (pincode.length() > 6) {
            pincode = pincode.substring(0, 6);
        }
        obj.setPincode(pincode);
    }

	public Staff getSpecificStaff(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Staff obj = data.get(index);
        Long id = obj.getId();
        return Staff.findStaff(id);
    }

	public Staff getRandomStaff() {
        init();
        Staff obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Staff.findStaff(id);
    }

	public boolean modifyStaff(Staff obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Staff.findStaffEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Staff' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Staff>();
        for (int i = 0; i < 10; i++) {
            Staff obj = getNewTransientStaff(i);
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
