package com.hmi.kiddos.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.hmi.kiddos.dao.AdmissionDao;

@Entity
@Configurable
@Audited
public class Admission implements Comparable {
	@Autowired
	@Transient
	private AdmissionDao admissionDao;

	public static final EntityManager entityManager() {
		EntityManager em = new Admission().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	private static Logger log = Logger.getLogger(Admission.class);

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
	@ManyToMany
	@Fetch(FetchMode.SUBSELECT)
	@BatchSize(size = 100)
	private Set<Program> programs = new TreeSet<Program>();

	public String toString() {
		return child.toString() + ", " + programs.toString();
	}

	@Transient
	private Set<Program> activePrograms = new TreeSet<Program>();

	public Set<Program> getActivePrograms() {
		Set<Program> programSet = new TreeSet<Program>();
		for (Program program : programs) {
			if (program.getDueDate().after(Calendar.getInstance()))
				programSet.add(program);
		}
		return programSet;
	}

	@Override

	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	@PersistenceContext
	transient EntityManager entityManager;


	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Admission attached = AdmissionDao.findAdmission(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	
	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Admission merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Admission merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	/**
	 */
	private int discount;

	/**
	 */
	private int admissionFee;

	public int getAdmissionFee() {
		return admissionFee;
	}

	public void setAdmissionFee(int admissionFee) {
		this.admissionFee = admissionFee;
	}

	@Transient
	private Integer feesExpected = 0;

	public Integer getFeesExpected() {
		feesExpected = 0;
		if (!programs.isEmpty()) {
			boolean sc = false;
			for (Program program : programs) {
				feesExpected += program.getFees();
				if (program.getType().equals("SC"))
					sc = true;
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
		log.info("Fees calculated: " + feesExpected + " , discount: " + discount + " admission fee: " + admissionFee);
		feesExpected = feesExpected - discount + admissionFee;
		return feesExpected;
	}

	public void setFeesExpected(Integer feesExpected) {
		this.feesExpected = feesExpected;
	}

	public Calendar getAdmissionDate() {
		return this.admissionDate;
	}

	public Date getAdmissionDateAsDate() {
		return (this.admissionDate == null) ? null : this.admissionDate.getTime();
	}

	public void setAdmissionDate(Calendar admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Calendar getJoiningDate() {
		return this.joiningDate;
	}

	public void setJoiningDate(Calendar joiningDate) {
		this.joiningDate = joiningDate;
	}


	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Child getChild() {
		return this.child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Set<Program> getPrograms() {
		return this.programs;
	}

	public Set<Program> getCurrentPrograms() {
		Set<Program> currentPrograms = new HashSet<Program>();
		for (Program program : programs) {
			if (program.isCurrent())
				currentPrograms.add(program);
		}
		return currentPrograms;
	}

	public void setPrograms(Set<Program> programs) {
		this.programs = programs;
	}

	public int getDiscount() {
		return this.discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	@Column(name = "UPDATE_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar updateTS;

	@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar creationTS;

}
