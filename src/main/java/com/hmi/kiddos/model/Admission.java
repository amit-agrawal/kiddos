package com.hmi.kiddos.model;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Audited
public class Admission implements Comparable {

    private static Logger log = Logger.getLogger(Admission.class);

    @Transient
    private Integer feesExpected = 0;

    @Transient
    private Integer feesDue = 0;

    @Transient
    private Integer feesPaid = 0;

    public Integer getFeesExpected() {
        feesExpected = 0;
        if (!programs.isEmpty()) {
            boolean sc = false;
            for (Program program : programs) {
                feesExpected += program.getFees();
                if (program.getType().equals("SC")) sc = true;
            }
            if (sc) {
                log.info("adjusting fees for SC");
                if (feesExpected == 1700) 
                	feesExpected = 3400; 
                else if (feesExpected == 5100) 
                	feesExpected = 5800; 
                else if (feesExpected == 6800) 
                	feesExpected = 5800;
            }
        }
        log.info("Fees calculated: " + feesExpected + " , discount: " + discount);
        feesExpected = feesExpected - discount;
        return feesExpected;
    }

    public Integer getFeesPaid() {
        feesPaid = 0;
        log.debug("Payments are: " + payments);
        if (!payments.isEmpty()) {
            for (Payment payment : payments) {
                feesPaid += payment.getAmount();
            }
        }
        return feesPaid;
    }

    public Integer getFeesDue() {
        return getFeesExpected() - getFeesPaid();
    }

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar admissionDate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar joiningDate;

    @ManyToOne(cascade = CascadeType.ALL)
    private Transportation transportArrival;

    @ManyToOne(cascade = CascadeType.ALL)
    private Transportation transportDeparture;

    /**
     */
    @Size(max = 400)
    private String notes;

    /**
     */
    @NotNull
    @ManyToOne
    private Child child;

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Program> programs = new TreeSet<Program>();

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "admissions")
    private Set<Payment> payments = new TreeSet<Payment>();

    public String toString() {
        return child.toString() + ", " + programs.toString();
    }

	@Transient
    private Set<Program> activePrograms = new TreeSet<Program>();
	
	public Set<Program> getActivePrograms() {
		Set<Program> programSet = new TreeSet<Program>();
		for(Program program: programs) {
			if (program.getDueDate().after(Calendar.getInstance()))
				programSet.add(program);
		}
		return programSet;
	}

    /**
     */
    private int discount;

	public Transportation getTransportArrival() {
		return transportArrival;
	}

	public Transportation getTransportDeparture() {
		return transportDeparture;
	}	@Override

	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}



}
