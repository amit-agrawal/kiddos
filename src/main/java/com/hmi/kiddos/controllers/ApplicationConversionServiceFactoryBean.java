package com.hmi.kiddos.controllers;

import org.springframework.format.FormatterRegistry;
import com.hmi.kiddos.model.*;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.annotations.converter.RooConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

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
}
