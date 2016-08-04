package com.hmi.kiddos.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

@Configurable
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "type", "term", "batch", "center" }) })
@Audited
public class Program implements Comparable {
	@Transient
	private Integer kidsCount = 0;

	public Integer getKidsCount() {
		return getChildren().size();
	}

	@Transient
	private Set<Child> children = new TreeSet<Child>();

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
		return (type.equals(ProgramTypes.JR_KG.toString()) || type.equals(ProgramTypes.NURSERY.toString())
				|| type.equals(ProgramTypes.PLAY_GROUP.toString()) || type.equals(ProgramTypes.ANNUAL_FEE.toString())
				|| type.equals(ProgramTypes.ADMISSION_FEE.toString()));
	}

	/**
	 */
	@ManyToMany(mappedBy = "programs")
	private Set<Admission> admissions = new TreeSet<Admission>();

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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("kidsCount", "children",
			"term", "type", "batch", "center", "dueDate", "fees", "notes", "teacher", "admissions");

	public static final EntityManager entityManager() {
		EntityManager em = new Program().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countPrograms() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Program o", Long.class).getSingleResult();
	}

	public static List<Program> findAllPrograms() {
		return entityManager().createQuery("SELECT o FROM Program o order by term, type", Program.class).getResultList();
	}

	public static List<Program> findAllActivePrograms() {
		return entityManager()
				.createQuery("SELECT o FROM Program o where due_date > current_date order by type, term, batch",
						Program.class)
				.getResultList();
	}

	public static List<Program> findOnlyActivePrograms() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and start_date <= current_date order by type, term, batch",
				Program.class).getResultList();
	}

	public static List<Program> findFuturePrograms() {
		return entityManager()
				.createQuery("SELECT o FROM Program o where start_date > current_date order by type, term, batch",
						Program.class)
				.getResultList();
	}

	public static List<Program> findAllPrograms(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Program o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Program.class).getResultList();
	}

	public static Program findProgram(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Program.class, id);
	}

	public static List<Program> findProgramEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Program o", Program.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Program> findProgramEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Program o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Program.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

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
			Program attached = Program.findProgram(this.id);
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

	public static List<Program> findAllPrograms(String status) {
		List<Program> allPrograms = findAllPrograms();
		List<Program> programList = new ArrayList<Program>();
		for (Program program : allPrograms) {
			if (status.equals("old")) {
				if ((program.getDueDate() != null) && program.getDueDate().before(Calendar.getInstance()))
					programList.add(program);
			} else if (status.equals("current")) {
				if (program.isCurrent())
					programList.add(program);

			} else if (status.equals("future")) {
				if ((program.getStartDate() != null) && program.getStartDate().after(Calendar.getInstance()))
					programList.add(program);
			}
		}
		return programList;
	}
}
