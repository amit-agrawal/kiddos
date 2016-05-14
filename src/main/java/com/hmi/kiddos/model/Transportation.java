package com.hmi.kiddos.model;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Transportation {

    @Override
	public String toString() {
    	if (driverName.equals("N.A."))
    		return "N.A.";
    	else
    		return "[" + driverName + ", " + notes + ", " + van + "]";
	}

	/**
     */
    @NotNull
    @Size(max = 80)
    private String driverName;

    /**
     */
    @Size(max = 400)
    private String notes;

    /**
     */
    @Size(max = 80)
    private String van;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportDeparture")
     private Set<Admission> departureAdmissions = new HashSet<Admission>();

	/**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transportArrival")
    private Set<Admission> arrivalAdmissions = new HashSet<Admission>();

	@Transient
    private Set<Child> departureChildren = new HashSet<Child>();
	
	public Set<Child> getDepartureChildren() {
		Set<Child> childrenSet = new HashSet<Child>();
		for(Admission admission: departureAdmissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}
	
	@Transient
    private Set<Child> arrivalChildren = new HashSet<Child>();

	public Set<Child> getArrivalChildren() {
		Set<Child> childrenSet = new HashSet<Child>();
		for(Admission admission: arrivalAdmissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}

    @Transient
    private Integer arrivalKidsCount = 0;
	
	public Integer getArrivalKidsCount() {
		return arrivalAdmissions.size();
	}
	
	@Transient
    private Integer departureKidsCount = 0;
	
	public Integer getDepartureKidsCount() {
		return departureAdmissions.size();
	}


}
