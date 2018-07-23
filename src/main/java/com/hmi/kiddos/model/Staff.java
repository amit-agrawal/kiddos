package com.hmi.kiddos.model;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.hmi.kiddos.model.enums.Centers;
import com.hmi.kiddos.model.enums.Department;
import com.hmi.kiddos.model.enums.Gender;

@Configurable
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"firstName", "middleName", "lastName"})})
@Audited
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Staff implements Comparable {

	@OneToMany(mappedBy="teacher")
	private Set<Program> primaryPrograms;

	public Set<Program> getPrimaryPrograms() {
		return primaryPrograms;
	}

	@OneToMany(mappedBy="teacherTwo")
	private Set<Program> secondaryPrograms;

	public Set<Program> getSecondaryPrograms() {
		return secondaryPrograms;
	}

	public void setSecondaryPrograms(Set<Program> secondaryPrograms) {
		this.secondaryPrograms = secondaryPrograms;
	}

	public Set<Program> getPrimaryCurrentPrograms() {
		Set<Program> primaryCurrentPrograms = new HashSet<>();
		for (Program program: primaryPrograms) {
			if (program.isCurrent())
				primaryCurrentPrograms.add(program);
		}
		return primaryCurrentPrograms;
	}

	public Set<Program> getSecondaryCurrentPrograms() {
		Set<Program> secondaryCurrentPrograms = new HashSet<>();
		for (Program program: secondaryPrograms) {
			if (program.isCurrent())
				secondaryCurrentPrograms.add(program);
		}
		return secondaryCurrentPrograms;
	}

	public void setPrimaryPrograms(Set<Program> primaryPrograms) {
		this.primaryPrograms = primaryPrograms;
	}

	/**
     */
    @NotNull
    @Size(max = 20)
    private String firstName;

    /**
     */
    @Size(max = 20)
    private String middleName;

    /**
     */
    @NotNull
    @Size(max = 20)
    private String lastName;

    /**
     */
    private String password;

    /**
     */
    @Enumerated
    private Gender gender;

    /**
     */
    @Size(max = 30)
    private String emailOne;

    /**
     */
    @Size(max = 20)
    private String phone;

    /**
     */
    @Size(max = 20)
    private String phoneHome;

    /**
     */
    @Size(max = 200)
    private String address;

    /**
     */
    @Size(max = 6)
    private String pincode;

    /**
     */
    @Enumerated
    private Centers center;

    /**
     */
    @Enumerated
    private Department department;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dob;

	@Transient
	private String dobToDisplay;

	public String getDobToDisplay() {
		String output = "";
		if (dob != null) {
			LocalDate birthday = getDobAsLocalDate();

			output = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		return output;
	}

	private LocalDate getDobAsLocalDate() {
		ZonedDateTime zdt = ZonedDateTime.ofInstant(dob.toInstant(), ZoneId.systemDefault());
		return zdt.toLocalDate();
	}

	public void setDobToDisplay(String dobToDisplay) {
		this.dobToDisplay = dobToDisplay;
	}

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar joiningDate;

    public Calendar getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Calendar joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Calendar getLastDate() {
		return lastDate;
	}

	public void setLastDate(Calendar lastDate) {
		this.lastDate = lastDate;
	}

	/**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar lastDate;

    /**
     */
    @Size(max = 400)
    private String notes;

    /**
     */
    @NotNull
    private Boolean enabled;

    /**
     */
    @ManyToMany
    private Set<UserRole> roles = new TreeSet<UserRole>();
    
    @PrePersist
    @PreUpdate
    protected void encryptPassword() {
      if (password != null && (! password.matches("^[0-9a-fA-F]+$"))) {
        // prevent encryption if already encrypted
        password = DigestUtils.md5DigestAsHex(password.getBytes());
      }
    }
    
    public String toString() {
		String output = firstName + " ";
		if (middleName != null && !middleName.isEmpty())
			output = output + middleName + " ";
		output = output + lastName;
    	return output;
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

	public String getFirstName() {
        return this.firstName;
    }

	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	public String getMiddleName() {
        return this.middleName;
    }

	public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

	public String getLastName() {
        return this.lastName;
    }

	public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public String getEmailOne() {
        return this.emailOne;
    }

	public void setEmailOne(String emailOne) {
        this.emailOne = emailOne;
    }

	public String getPhone() {
        return this.phone;
    }

	public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getPhoneHome() {
        return this.phoneHome;
    }

	public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

	public String getAddress() {
        return this.address;
    }

	public void setAddress(String address) {
        this.address = address;
    }

	public String getPincode() {
        return this.pincode;
    }

	public void setPincode(String pincode) {
        this.pincode = pincode;
    }

	public Centers getCenter() {
        return this.center;
    }

	public void setCenter(Centers center) {
        this.center = center;
    }

	public Department getDepartment() {
        return this.department;
    }

	public void setDepartment(Department department) {
        this.department = department;
    }

	public Calendar getDob() {
        return this.dob;
    }

	public void setDob(Calendar dob) {
        this.dob = dob;
    }

	public String getNotes() {
        return this.notes;
    }

	public void setNotes(String notes) {
        this.notes = notes;
    }

	public Boolean getEnabled() {
        return this.enabled;
    }

	public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public Set<UserRole> getRoles() {
        return this.roles;
    }

	public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("firstName", "middleName", "lastName", "password", "gender", "emailOne", "phone", "phoneHome", "address", "pincode", "center", "department", "dob", "notes", "enabled", "roles");

	public static final EntityManager entityManager() {
        EntityManager em = new Staff().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStaffs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Staff o", Long.class).getSingleResult();
    }

	public static List<Staff> findAllStaffs() {
        return entityManager().createQuery("SELECT o FROM Staff o", Staff.class).getResultList();
    }

	public static List<Staff> findAllActiveStaffs() {
        return entityManager().createQuery("SELECT o FROM Staff o where enabled = 1", Staff.class).getResultList();
    }

	public boolean isTeacherOrTrainer() {
		return (department == Department.Teacher || department == Department.Trainer);
	}
	
 	public static List<Staff> findAllActiveTeacherTrainers() {
        List<Staff> activeStaff = entityManager().createQuery("SELECT o FROM Staff o where enabled = 1", Staff.class).getResultList();
        return activeStaff.stream()
        			.filter(staff -> staff.isTeacherOrTrainer())
        			.collect(Collectors.toList());
    }

	public static List<Staff> findAllStaffs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Staff o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Staff.class).getResultList();
    }

	public static List<Staff> findAllActiveStaffs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Staff o where is_active = 1";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Staff.class).getResultList();
    }

	public static Staff findStaff(Long id) {
        if (id == null) return null;
        return entityManager().find(Staff.class, id);
    }

	public static List<Staff> findStaffEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Staff o", Staff.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Staff> findActiveStaffEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Staff o where is_active = 1", Staff.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Staff> findStaffEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Staff o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Staff.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Staff> findActiveStaffEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Staff o where is_active = 1";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Staff.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Staff attached = Staff.findStaff(this.id);
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
    public Staff merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Staff merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Column(name="UPDATE_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar updateTS;
			     
	@Column(name="CREATION_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar creationTS;


}

