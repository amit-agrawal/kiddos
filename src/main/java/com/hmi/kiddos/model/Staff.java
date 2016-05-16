package com.hmi.kiddos.model;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.Enumerated;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.TreeSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Audited
public class Staff implements Comparable {

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
    @ManyToMany(cascade = CascadeType.ALL)
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
		if (! middleName.isEmpty())
			output = output + middleName + " ";
		output = output + lastName;
    	return output;
    }

	@Override
	public int compareTo(Object other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

}

