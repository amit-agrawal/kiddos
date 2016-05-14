package com.hmi.kiddos.model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Program {	
	@Transient
    private Integer kidsCount = 0;
	
	public Integer getKidsCount() {
		return admissions.size();
	}
	
	@Transient
    private Set<Child> children = new HashSet<Child>();
	
	public Set<Child> getChildren() {
		Set<Child> childrenSet = new HashSet<Child>();
		for(Admission admission: admissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}

	/**
     */    
    @NotNull
    private String term;

    /**
     */
    @NotNull
    private String type;

    /**
     */
    private String batch = "Undecided";

    /**
     */
    @Enumerated
    private Centers center = Centers.Bhandup;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dueDate;
    
    /**
     */
    @Min(1L)
    @Max(999999L)
    private Integer fees;

    /**
     */
    private String notes;

    /**
     */
    @ManyToOne
    private Staff teacher;

    public boolean isPreSchool() {
    	return (type.equals(ProgramTypes.JR_KG.toString()) || 
    			type.equals(ProgramTypes.NURSERY.toString()) || 
    			type.equals(ProgramTypes.PLAY_GROUP.toString()) || 
    			type.equals(ProgramTypes.ANNUAL_FEE.toString()) || 
    			type.equals(ProgramTypes.ADMISSION_FEE.toString()));
    }
    
    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "programs")
    private Set<Admission> admissions = new HashSet<Admission>();
    
    public String toString() {
		String output = type + " : " + term;
		if (batch != null)
			output = output + " : " + batch;
    	return output;
    }
}
