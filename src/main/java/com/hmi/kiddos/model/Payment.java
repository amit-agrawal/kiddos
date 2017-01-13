package com.hmi.kiddos.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Audited
public class Payment {

	/**
	 */
	@Max(999999L)
	@NotNull
	private Integer amount;

	/**
	 */
	@ManyToOne
	private Child child;

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
	@Size(max = 100)
	private String transactionNumber;

	/**
	 */
	@Size(max = 30)
	private String receiptNumber;

	/**
	 */
	private String notes;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 */
	@ManyToMany
	private Set<Program> programs = new TreeSet<Program>();

	@Transient
	private Set<Program> daycarePrograms = new TreeSet<Program>();

	@Transient
	private Set<Program> preschoolPrograms = new TreeSet<Program>();

	@Transient
	private Set<Program> charges = new TreeSet<Program>();

	@Transient
	private Set<Program> otherPrograms = new TreeSet<Program>();

	public Set<Program> getDaycarePrograms() {
		return daycarePrograms;
	}

	public void setDaycarePrograms(Set<Program> daycarePrograms) {
		this.daycarePrograms = daycarePrograms;
	}

	public Set<Program> getPreschoolPrograms() {
		return preschoolPrograms;
	}

	public void setPreschoolPrograms(Set<Program> preschoolPrograms) {
		this.preschoolPrograms = preschoolPrograms;
	}

	public Set<Program> getCharges() {
		return charges;
	}

	public void setCharges(Set<Program> charges) {
		this.charges = charges;
	}

	public Set<Program> getOtherPrograms() {
		return otherPrograms;
	}

	public void setOtherPrograms(Set<Program> otherPrograms) {
		this.otherPrograms = otherPrograms;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Set<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(Set<Program> programs) {
		this.programs = programs;
	}

	@Override
	public String toString() {
		String output = "Payment [amount=" + amount + ", paymentMedium=" + paymentMedium + ", receiptNumber=" + id;
		if (programs != null && !programs.isEmpty())
			output = output + ", Programs:" + programs;
		if (child != null)
			output = output + ", Child:" + child;
		
		output = output + "]";
		return output;
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

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public PaymentMedium getPaymentMedium() {
		return this.paymentMedium;
	}

	public void setPaymentMedium(PaymentMedium paymentMedium) {
		this.paymentMedium = paymentMedium;
	}

	public Calendar getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Calendar paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getTransactionNumber() {
		return this.transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getReceiptNumber() {
		return this.receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("amount", "paymentMedium",
			"paymentDate", "transactionNumber", "receiptNumber", "admissions");

	public static final EntityManager entityManager() {
		EntityManager em = new Payment().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countPayments() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Payment o", Long.class).getSingleResult();
	}

	public static List<Payment> findAllPayments() {
		return entityManager().createQuery("SELECT o FROM Payment o", Payment.class).getResultList();
	}

	public static List<Payment> findAllPayments(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Payment o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Payment.class).getResultList();
	}

	public static Payment findPayment(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Payment.class, id);
	}

	public static List<Payment> findPaymentEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Payment o", Payment.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Payment> findPaymentEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Payment o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Payment.class).setFirstResult(firstResult)
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
			Payment attached = Payment.findPayment(this.id);
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
	public Payment merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Payment merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@Column(name = "UPDATE_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar updateTS;

	@Column(name = "CREATION_TS", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar creationTS;

}
