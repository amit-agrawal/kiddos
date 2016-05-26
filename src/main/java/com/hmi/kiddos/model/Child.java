package com.hmi.kiddos.model;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.hmi.kiddos.dao.ChildDao;
	
@Configurable
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"firstName", "middleName", "lastName", "dob"})})
@Audited
public class Child implements Comparable {
	

	@Transient
	private String age;
	
    /**
     */
    @NotNull
    @Size(max = 30)
    private String firstName;

    /**
     */
    @Size(max = 30)
    private String middleName;

    /**
     */
    @NotNull
    @Size(max = 30)
    private String lastName;

    /**
     */
    @Enumerated
    private Gender gender;

    /**
     */
    @Size(max = 60)
    private String fatherName;

    /**
     */
    @Size(max = 60)
    private String motherName;

    /**
     */
    @Size(max = 50)
    private String fatherOrganization;

    /**
     */
    @Size(max = 50)
    private String motherOrganization;

    /**
     */
    @Size(max = 100)
    private String emailOne;

    /**
     */
    @Size(max = 100)
    private String emailTwo;

    /**
     */
    @Size(max = 100)
    private String emailThree;

    /**
     */
    @Size(max = 30)
    private String phoneFather;

    /**
     */
    @Size(max = 30)
    private String phoneMother;

    /**
     */
    @Size(max = 30)
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
    @Size(max = 20)
    private String bloodGroup;

    /**
     */
    @Size(max = 150)
    private String allergy;

    /**
     */
    @Size(max = 50)
    private String nationality;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar dob;

    /**
     */
    @Size(max = 400)
    private String notes;

    @Transient
    private Set<Transportation> transportations = new TreeSet<Transportation>();
    
    public Set<Transportation> getTransportations() {
    	Set<Transportation> transportationSet = new TreeSet<Transportation>();
		for(Admission admission: admissions) {
			transportationSet.add(admission.getTransportArrival());
			transportationSet.add(admission.getTransportDeparture());
		}
		return transportationSet;
    }
    
    @Transient
    private Set<Program> programs = new TreeSet<Program>();
    
    public Set<Program> getPrograms() {
		Set<Program> programSet = new TreeSet<Program>();
		for(Admission admission: admissions)
			programSet.addAll(admission.getPrograms());
		return programSet;
    }
    
    /**
     */
    @OneToMany(mappedBy = "child")
    private Set<Admission> admissions = new TreeSet<Admission>();
    
    private LocalDate getDobAsLocalDate() {
    	ZonedDateTime zdt = ZonedDateTime.ofInstant(dob.toInstant(), ZoneId.systemDefault());
    	return zdt.toLocalDate();
    }
    
    public String getAge() {
		String output = "";
		if (dob != null) {
			LocalDate today = LocalDate.now();
			LocalDate birthday = getDobAsLocalDate();
			 
			Period period = Period.between(birthday, today);
			output = period.getYears() + " yrs " + period.getMonths() + " mths";
		}
    	return output;    	
    }
    
    public String toString() {
		String output = firstName + " ";
		if (! middleName.isEmpty())
			output = output + middleName + " ";
		output = output + lastName;
		if (dob != null) {
			output = output + ", " + getAge();
		}
    	return output;
    }

	@Override
	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

	public boolean hasProgram(String type) {
		Set<Program> programs = getPrograms();
		boolean hasProgram = false;
		if (type != null & (type.startsWith("PS"))) {
			for(Program program : programs) {
				if (program.getType().startsWith("Jr") || program.getType().startsWith("Pl") || program.getType().startsWith("Nu")) {
					hasProgram = true;
					break;
				}
			}
		}
		else {
			for(Program program : programs) {
				if (program.getType().startsWith(type)) {
					hasProgram = true;
					break;
				}
			}
		}
		
		return hasProgram;
	}

	@PersistenceContext
    public transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Child().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove(ChildDao childDao) {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Child attached = childDao.findChild(this.id);
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
    public Child merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Child merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

	public void setAge(String age) {
        this.age = age;
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

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public String getFatherName() {
        return this.fatherName;
    }

	public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

	public String getMotherName() {
        return this.motherName;
    }

	public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

	public String getFatherOrganization() {
        return this.fatherOrganization;
    }

	public void setFatherOrganization(String fatherOrganization) {
        this.fatherOrganization = fatherOrganization;
    }

	public String getMotherOrganization() {
        return this.motherOrganization;
    }

	public void setMotherOrganization(String motherOrganization) {
        this.motherOrganization = motherOrganization;
    }

	public String getEmailOne() {
        return this.emailOne;
    }

	public void setEmailOne(String emailOne) {
        this.emailOne = emailOne;
    }

	public String getEmailTwo() {
        return this.emailTwo;
    }

	public void setEmailTwo(String emailTwo) {
        this.emailTwo = emailTwo;
    }

	public String getEmailThree() {
        return this.emailThree;
    }

	public void setEmailThree(String emailThree) {
        this.emailThree = emailThree;
    }

	public String getPhoneFather() {
        return this.phoneFather;
    }

	public void setPhoneFather(String phoneFather) {
        this.phoneFather = phoneFather;
    }

	public String getPhoneMother() {
        return this.phoneMother;
    }

	public void setPhoneMother(String phoneMother) {
        this.phoneMother = phoneMother;
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

	public String getBloodGroup() {
        return this.bloodGroup;
    }

	public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

	public String getAllergy() {
        return this.allergy;
    }

	public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

	public String getNationality() {
        return this.nationality;
    }

	public void setNationality(String nationality) {
        this.nationality = nationality;
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

	public void setTransportations(Set<Transportation> transportations) {
        this.transportations = transportations;
    }

	public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

	public Set<Admission> getAdmissions() {
        return this.admissions;
    }

	public void setAdmissions(Set<Admission> admissions) {
        this.admissions = admissions;
    }
	
	@Column(name="UPDATE_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar updateTS;
			     
	@Column(name="CREATION_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar creationTS;
}
