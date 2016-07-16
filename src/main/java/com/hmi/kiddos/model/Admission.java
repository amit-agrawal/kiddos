package com.hmi.kiddos.model;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
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
        log.info("Fees calculated: " + feesExpected + " , discount: " + discount + " admission fee: " + admissionFee);
        feesExpected = feesExpected - discount + admissionFee;
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

    @ManyToOne
    private Transportation transportArrival;

    @ManyToOne
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
    @ManyToMany
	@Fetch(FetchMode.SUBSELECT)
	@BatchSize(size=100)
    private Set<Program> programs = new TreeSet<Program>();

    /**
     */
    @ManyToMany(mappedBy = "admissions")
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

    /**
     */
    private int admissionFee;

	public int getAdmissionFee() {
		return admissionFee;
	}

	public void setAdmissionFee(int admissionFee) {
		this.admissionFee = admissionFee;
	}

	public Transportation getTransportArrival() {
		return transportArrival;
	}

	public Transportation getTransportDeparture() {
		return transportDeparture;
	}	@Override

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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("log", "feesExpected", "feesDue", "feesPaid", "admissionDate", "joiningDate", "transportArrival", "transportDeparture", "notes", "child", "programs", "payments", "discount");

	public static final EntityManager entityManager() {
        EntityManager em = new Admission().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAdmissions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Admission o", Long.class).getSingleResult();
    }

	public static List<Admission> findAllAdmissions() {
        return entityManager().createQuery("SELECT o FROM Admission o order by o.id desc ", Admission.class).getResultList();
    }

	public static List<Admission> findAllAdmissions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Admission o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        else {
        	jpaQuery = jpaQuery + " order by o.id desc ";
        }
        return entityManager().createQuery(jpaQuery, Admission.class).getResultList();
    }

	public static Admission findAdmission(Long id) {
        if (id == null) return null;
        return entityManager().find(Admission.class, id);
    }

	public static List<Admission> findAdmissionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Admission o order by o.id desc ", Admission.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Admission> findAdmissionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Admission o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        else {
        	jpaQuery = jpaQuery + " order by o.id desc ";
        }
        return entityManager().createQuery(jpaQuery, Admission.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Admission attached = Admission.findAdmission(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Admission merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Admission merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public void setFeesExpected(Integer feesExpected) {
        this.feesExpected = feesExpected;
    }

	public void setFeesDue(Integer feesDue) {
        this.feesDue = feesDue;
    }

	public void setFeesPaid(Integer feesPaid) {
        this.feesPaid = feesPaid;
    }

	public Calendar getAdmissionDate() {
        return this.admissionDate;
    }

	public Date getAdmissionDateAsDate() {
        return (this.admissionDate == null)? null : this.admissionDate.getTime();
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

	public void setTransportArrival(Transportation transportArrival) {
        this.transportArrival = transportArrival;
    }

	public void setTransportDeparture(Transportation transportDeparture) {
        this.transportDeparture = transportDeparture;
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

	public Set<Payment> getPayments() {
        return this.payments;
    }

	public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

	public int getDiscount() {
        return this.discount;
    }

	public void setDiscount(int discount) {
        this.discount = discount;
    }
	
	@Column(name="UPDATE_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar updateTS;
			     
	@Column(name="CREATION_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar creationTS;

}
