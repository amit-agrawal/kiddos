package com.hmi.kiddos.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.hmi.kiddos.dao.AdmissionDao;

@Entity
@Configurable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "child", "program" }) })
@Audited
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
	@ManyToOne
	private Program program;

	public String toString() {
		return ((child == null) ? "" : child.toString()) + ", " + ((program == null) ? "" : program.toString());
	}

	public Program getActivePrograms() {
		if (program.getDueDate().after(Calendar.getInstance()))
			return program;
		else
			return null;
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
		if (program != null)
			feesExpected += program.getFees();
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

	public Program getProgram() {
		return this.program;
	}

	public Program getCurrentPrograms() {
		if (program.isCurrent())
			return program;
		else
			return null;
	}

	public Program getCurrentOrFuturePrograms() {
		if (program.isCurrentOrFuture())
			return program;
		else
			return null;
	}

	public void setProgram(Program program) {
		this.program = program;
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

	public boolean hasCurrentProgram(String type) {
		boolean hasProgram = false;
		if (program.isCurrent()) {
			if (type != null & (type.startsWith("PS"))) {
				if (program.getType().startsWith("Jr") || program.getType().startsWith("Pl")
						|| program.getType().startsWith("Nu")) {
					hasProgram = true;
				}
			} else if (type != null & (type.startsWith("DC"))) {
				if (program.getType().startsWith("DC") || program.getType().startsWith("IC")) {
					hasProgram = true;
				}
			} else {
				if (program.getType().startsWith(type)) {
					hasProgram = true;
				}
			}
		}

		return hasProgram;
	}

}
