package com.hmi.kiddos.model;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.Enumerated;

import java.time.*;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.TreeSet;
import java.util.Locale;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Audited
public class Child implements Comparable {

	@Transient
	private String age;
	
    /**
     */
    @NotNull
    @Size(max = 30)
    private String firstName;

    /**
     */
    @Size(max = 30)
    private String middleName;

    /**
     */
    @NotNull
    @Size(max = 30)
    private String lastName;

    /**
     */
    @Enumerated
    private Gender gender;

    /**
     */
    @Size(max = 60)
    private String fatherName;

    /**
     */
    @Size(max = 60)
    private String motherName;

    /**
     */
    @Size(max = 50)
    private String fatherOrganization;

    /**
     */
    @Size(max = 50)
    private String motherOrganization;

    /**
     */
    @Size(max = 100)
    private String emailOne;

    /**
     */
    @Size(max = 100)
    private String emailTwo;

    /**
     */
    @Size(max = 100)
    private String emailThree;

    /**
     */
    @Size(max = 30)
    private String phoneFather;

    /**
     */
    @Size(max = 30)
    private String phoneMother;

    /**
     */
    @Size(max = 30)
    private String phoneHome;

    /**
     */
    @Size(max = 200)
    private String address;

    /**
     */
    @Size(max = 6)
    private String pincode;

    /**
     */
    @Size(max = 20)
    private String bloodGroup;

    /**
     */
    @Size(max = 150)
    private String allergy;

    /**
     */
    @Size(max = 50)
    private String nationality;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dob;

    /**
     */
    @Size(max = 400)
    private String notes;

    @Transient
    private Set<Transportation> transportations = new TreeSet<Transportation>();
    
    public Set<Transportation> getTransportations() {
    	Set<Transportation> transportationSet = new TreeSet<Transportation>();
		for(Admission admission: admissions) {
			transportationSet.add(admission.getTransportArrival());
			transportationSet.add(admission.getTransportDeparture());
		}
		return transportationSet;
    }
    
    @Transient
    private Set<Program> programs = new TreeSet<Program>();
    
    public Set<Program> getPrograms() {
		Set<Program> programSet = new TreeSet<Program>();
		for(Admission admission: admissions)
			programSet.addAll(admission.getPrograms());
		return programSet;
    }
    
    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "child")
    private Set<Admission> admissions = new TreeSet<Admission>();
    
    private LocalDate getDobAsLocalDate() {
    	ZonedDateTime zdt = ZonedDateTime.ofInstant(dob.toInstant(), ZoneId.systemDefault());
    	return zdt.toLocalDate();
    }
    
    public String getAge() {
		String output = "";
		if (dob != null) {
			LocalDate today = LocalDate.now();
			LocalDate birthday = getDobAsLocalDate();
			 
			Period period = Period.between(birthday, today);
			output = period.getYears() + " yrs " + period.getMonths() + " mths";
		}
    	return output;    	
    }
    
    public String toString() {
		String output = firstName + " ";
		if (! middleName.isEmpty())
			output = output + middleName + " ";
		output = output + lastName;
		if (dob != null) {
			output = output + ", " + getAge();
		}
    	return output;
    }

	@Override
	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

}
