package com.hmi.kiddos.model;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;
import java.util.TreeSet;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Configurable
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"driverName", "van", "notes"})})
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Audited
public class Transportation implements Comparable {

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
    @OneToMany(mappedBy = "transportDeparture")
     private Set<Admission> departureAdmissions = new TreeSet<Admission>();

	/**
     */
    @OneToMany(mappedBy = "transportArrival")
    private Set<Admission> arrivalAdmissions = new TreeSet<Admission>();

	@Transient
    private Set<Child> departureChildren = new TreeSet<Child>();
	
	public Set<Child> getDepartureChildren() {
		Set<Child> childrenSet = new TreeSet<Child>();
		for(Admission admission: departureAdmissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}
	
	@Transient
    private Set<Child> arrivalChildren = new TreeSet<Child>();

	public Set<Child> getArrivalChildren() {
		Set<Child> childrenSet = new TreeSet<Child>();
		for(Admission admission: arrivalAdmissions)
			childrenSet.add(admission.getChild());
		return childrenSet;
	}

    @Transient
    private Integer allKidsCount = 0;
	
	public Integer getAllKidsCount() {
		return getAllChildren().size();
	}

	@Transient
    private Set<Child> allChildren = new TreeSet<Child>();

	public Set<Child> getAllChildren() {
		Set<Child> childrenSet = new TreeSet<Child>();
		for(Admission admission: arrivalAdmissions)
			childrenSet.add(admission.getChild());
		for(Admission admission: departureAdmissions)
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

	@Override
	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}


	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("driverName", "notes", "van", "departureAdmissions", "arrivalAdmissions", "departureChildren", "arrivalChildren", "arrivalKidsCount", "departureKidsCount");

	public static final EntityManager entityManager() {
        EntityManager em = new Transportation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTransportations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Transportation o", Long.class).getSingleResult();
    }

	public static List<Transportation> findAllTransportations() {
        return entityManager().createQuery("SELECT o FROM Transportation o", Transportation.class).getResultList();
    }

	public static List<Transportation> findAllTransportations(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Transportation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Transportation.class).getResultList();
    }

	public static Transportation findTransportation(Long id) {
        if (id == null) return null;
        return entityManager().find(Transportation.class, id);
    }

	public static List<Transportation> findTransportationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Transportation o", Transportation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Transportation> findTransportationEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Transportation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Transportation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Transportation attached = Transportation.findTransportation(this.id);
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
    public Transportation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Transportation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getDriverName() {
        return this.driverName;
    }

	public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

	public String getNotes() {
        return this.notes;
    }

	public void setNotes(String notes) {
        this.notes = notes;
    }

	public String getVan() {
        return this.van;
    }

	public void setVan(String van) {
        this.van = van;
    }

	public Set<Admission> getDepartureAdmissions() {
        return this.departureAdmissions;
    }

	public void setDepartureAdmissions(Set<Admission> departureAdmissions) {
        this.departureAdmissions = departureAdmissions;
    }

	public Set<Admission> getArrivalAdmissions() {
        return this.arrivalAdmissions;
    }

	public void setArrivalAdmissions(Set<Admission> arrivalAdmissions) {
        this.arrivalAdmissions = arrivalAdmissions;
    }

	public void setDepartureChildren(Set<Child> departureChildren) {
        this.departureChildren = departureChildren;
    }

	public void setArrivalChildren(Set<Child> arrivalChildren) {
        this.arrivalChildren = arrivalChildren;
    }

	public void setArrivalKidsCount(Integer arrivalKidsCount) {
        this.arrivalKidsCount = arrivalKidsCount;
    }

	public void setDepartureKidsCount(Integer departureKidsCount) {
        this.departureKidsCount = departureKidsCount;
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

	@Column(name="UPDATE_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar updateTS;
			     
	@Column(name="CREATION_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar creationTS;

	public static List<Transportation> findAllDropTransportations() {
        String jpaQuery = "SELECT o FROM Transportation o where van in ('N.A.', 'Drop') ";
        return entityManager().createQuery(jpaQuery, Transportation.class).getResultList();
	}

	public static List<Transportation> findAllPickupTransportations() {
        String jpaQuery = "SELECT o FROM Transportation o where van in ('N.A.', 'Pickup') ";
        return entityManager().createQuery(jpaQuery, Transportation.class).getResultList();
	}

}
