package com.hmi.kiddos.model;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.hmi.kiddos.dao.ProgramDao;

@Configurable
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "type", "term", "batch", "center", "notes" }) })
@Audited
public class Program implements Comparable {
	@Transient
	private Integer kidsCount = 0;

	public Integer getKidsCount() {
		return getChildren().size();
	}

	private boolean isCharge = false;

	private String programType;
	
	public boolean getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(boolean isCharge) {
		this.isCharge = isCharge;
	}

	@Transient
	private Set<Child> children = new TreeSet<Child>();

	public String getName() {
		String output = type + ", " + term;
		if (batch != null)
			output = output + ", " + batch;
		return output;

	}

	public Set<Child> getChildren() {
		Set<Child> childrenSet = new TreeSet<Child>();
		for (Admission admission : admissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}

	public boolean isCurrent() {
		if (((getDueDate() == null) || getDueDate().after(Calendar.getInstance()))
				&& ((getStartDate() == null) || getStartDate().before(Calendar.getInstance())))
			return true;
		else
			return false;
	}

	public boolean isCurrentOrFuture() {
		if ((getDueDate() == null) || getDueDate().after(Calendar.getInstance()))
			return true;
		else
			return false;
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
	@Column(name = "DUE_DATE")
	private Calendar dueDate;

	/**
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Calendar startDate;

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

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

	/**
	 */
	@ManyToOne
	private Staff teacherTwo;

	public Staff getTeacherTwo() {
		return teacherTwo;
	}

	public void setTeacherTwo(Staff teacherTwo) {
		this.teacherTwo = teacherTwo;
	}

	public boolean isPreSchool() {
		return "P".equals(programType);
	}

	/**
	 */
	@OneToMany(mappedBy = "program")
	private Set<Admission> admissions = new TreeSet<Admission>();

/*	*//**
	 *//*
	@ManyToMany(mappedBy = "programs")
	private Set<Payment> payments = new TreeSet<Payment>();
*/
/*	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}
*/
	public String toString() {
		String output = type + " : " + term;
		if (batch != null)
			output = output + " : " + batch;
		return output;
	}

	@Override
	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Program().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove(ProgramDao programDao) {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Program attached = programDao.findProgram(this.id);
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
	public Program merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Program merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public void setKidsCount(Integer kidsCount) {
		this.kidsCount = kidsCount;
	}

	public void setChildren(Set<Child> children) {
		this.children = children;
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Centers getCenter() {
		return this.center;
	}

	public void setCenter(Centers center) {
		this.center = center;
	}

	public Calendar getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getFees() {
		return this.fees;
	}

	public void setFees(Integer fees) {
		this.fees = fees;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Staff getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Staff teacher) {
		this.teacher = teacher;
	}

	public Set<Admission> getAdmissions() {
		return this.admissions;
	}

	public void setAdmissions(Set<Admission> admissions) {
		this.admissions = admissions;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public void setCharge(boolean isCharge) {
		this.isCharge = isCharge;
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

	@Column(name = "UPDATE_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar updateTS;

	@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar creationTS;

}
