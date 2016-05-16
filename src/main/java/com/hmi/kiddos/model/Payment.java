package com.hmi.kiddos.model;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import java.util.Calendar;
import java.util.TreeSet;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Audited
public class Payment {

    /**
     */
    @Max(999999L)
    @NotNull
    private Integer amount;

    /**
     */
    @Enumerated
    @NotNull
    private PaymentMedium paymentMedium;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar paymentDate;

    /**
     */
    @Size(max = 50)
    private String transactionNumber;

    /**
     */
    @Size(max = 30)
    private String receiptNumber;

    /**
     */
    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Admission> admissions = new TreeSet<Admission>();

	@Override
	public String toString() {
		return "Payment [amount=" + amount + ", paymentMedium=" + paymentMedium + ", receiptNumber=" + receiptNumber + "]";
	}
    
}
