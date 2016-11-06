package com.hmi.kiddos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.hmi.kiddos.dao.AdmissionDao;
import com.hmi.kiddos.dao.ChildDao;
import com.hmi.kiddos.dao.ProgramDao;
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Child;
import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.model.Staff;
import com.hmi.kiddos.model.Transportation;
import com.hmi.kiddos.model.UserRole;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {
	@Autowired
	private ChildDao childDao;

	@Autowired
	private AdmissionDao admissionDao;
	
	@Autowired
	private ProgramDao programDao;

    public Converter<Admission, String> getAdmissionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Admission, java.lang.String>() {
            public String convert(Admission admission) {
                return admission.toString();
            }
        };
    }

    public Converter<Child, String> getChildToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Child, java.lang.String>() {
            public String convert(Child child) {
                return child.toString();
            }
        };
    }
    public Converter<Payment, String> getPaymentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Payment, java.lang.String>() {
            public String convert(Payment payment) {
                return payment.toString();
            }
        };
    }

    public Converter<Staff, String> getStaffToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Staff, java.lang.String>() {
            public String convert(Staff staff) {
                return staff.toString();
            }
        };
    }
    
    public Converter<UserRole, String> getUserRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.UserRole, java.lang.String>() {
            public String convert(UserRole userRole) {
                return userRole.toString();
            }
        };
    }


	private Converter<Transportation, String> getTransportationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Transportation, java.lang.String>() {
            public String convert(Transportation transportation) {
                return transportation.toString();
            }
        };
	}

	public Converter<Program, String> getProgramToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.hmi.kiddos.model.Program, java.lang.String>() {
            public String convert(Program program) {
                return program.toString();
            }
        };
    }
 
    
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
        registry.addConverter(getAdmissionToStringConverter());
        registry.addConverter(getChildToStringConverter());
        registry.addConverter(getPaymentToStringConverter());
        registry.addConverter(getProgramToStringConverter());
        registry.addConverter(getStaffToStringConverter());
        registry.addConverter(getUserRoleToStringConverter());
        registry.addConverter(getTransportationToStringConverter());
	}

	public Converter<Long, Admission> getIdToAdmissionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Admission>() {
            public com.hmi.kiddos.model.Admission convert(java.lang.Long id) {
                return admissionDao.findAdmission(id);
            }
        };
    }

	public Converter<String, Admission> getStringToAdmissionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Admission>() {
            public com.hmi.kiddos.model.Admission convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Admission.class);
            }
        };
    }

	public Converter<Long, Child> getIdToChildConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Child>() {
            public com.hmi.kiddos.model.Child convert(java.lang.Long id) {
                return childDao.findChild(id);
            }
        };
    }

	public Converter<String, Child> getStringToChildConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Child>() {
            public com.hmi.kiddos.model.Child convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Child.class);
            }
        };
    }

	public Converter<Long, Payment> getIdToPaymentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Payment>() {
            public com.hmi.kiddos.model.Payment convert(java.lang.Long id) {
                return Payment.findPayment(id);
            }
        };
    }

	public Converter<String, Payment> getStringToPaymentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Payment>() {
            public com.hmi.kiddos.model.Payment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Payment.class);
            }
        };
    }

	public Converter<Long, Program> getIdToProgramConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Program>() {
            public com.hmi.kiddos.model.Program convert(java.lang.Long id) {
                return programDao.findProgram(id);
            }
        };
    }

	public Converter<String, Program> getStringToProgramConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Program>() {
            public com.hmi.kiddos.model.Program convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Program.class);
            }
        };
    }

	public Converter<Long, Staff> getIdToStaffConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Staff>() {
            public com.hmi.kiddos.model.Staff convert(java.lang.Long id) {
                return Staff.findStaff(id);
            }
        };
    }

	public Converter<String, Staff> getStringToStaffConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Staff>() {
            public com.hmi.kiddos.model.Staff convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Staff.class);
            }
        };
    }

	public Converter<Long, Transportation> getIdToTransportationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.Transportation>() {
            public com.hmi.kiddos.model.Transportation convert(java.lang.Long id) {
                return Transportation.findTransportation(id);
            }
        };
    }

	public Converter<String, Transportation> getStringToTransportationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.Transportation>() {
            public com.hmi.kiddos.model.Transportation convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Transportation.class);
            }
        };
    }

	public Converter<Long, UserRole> getIdToUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.hmi.kiddos.model.UserRole>() {
            public com.hmi.kiddos.model.UserRole convert(java.lang.Long id) {
                return UserRole.findUserRole(id);
            }
        };
    }

	public Converter<String, UserRole> getStringToUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.hmi.kiddos.model.UserRole>() {
            public com.hmi.kiddos.model.UserRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UserRole.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAdmissionToStringConverter());
        registry.addConverter(getIdToAdmissionConverter());
        registry.addConverter(getStringToAdmissionConverter());
        registry.addConverter(getChildToStringConverter());
        registry.addConverter(getIdToChildConverter());
        registry.addConverter(getStringToChildConverter());
        registry.addConverter(getPaymentToStringConverter());
        registry.addConverter(getIdToPaymentConverter());
        registry.addConverter(getStringToPaymentConverter());
        registry.addConverter(getProgramToStringConverter());
        registry.addConverter(getIdToProgramConverter());
        registry.addConverter(getStringToProgramConverter());
        registry.addConverter(getStaffToStringConverter());
        registry.addConverter(getIdToStaffConverter());
        registry.addConverter(getStringToStaffConverter());
        registry.addConverter(getTransportationToStringConverter());
        registry.addConverter(getIdToTransportationConverter());
        registry.addConverter(getStringToTransportationConverter());
        registry.addConverter(getUserRoleToStringConverter());
        registry.addConverter(getIdToUserRoleConverter());
        registry.addConverter(getStringToUserRoleConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
