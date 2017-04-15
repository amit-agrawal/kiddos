package com.hmi.kiddos.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class PaymentDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Payment> data;

	public Payment getNewTransientPayment(int index) {
        Payment obj = new Payment();
        setAmount(obj, index);
        setPaymentDate(obj, index);
        setPaymentMedium(obj, index);
        setTransactionNumber(obj, index);
        return obj;
    }

	public void setAmount(Payment obj, int index) {
        Integer amount = new Integer(index);
        if (amount > 999999) {
            amount = 999999;
        }
        obj.setAmount(amount);
    }

	public void setPaymentDate(Payment obj, int index) {
        Calendar paymentDate = Calendar.getInstance();
        obj.setPaymentDate(paymentDate);
    }

	public void setPaymentMedium(Payment obj, int index) {
        PaymentMedium paymentMedium = PaymentMedium.class.getEnumConstants()[0];
        obj.setPaymentMedium(paymentMedium);
    }

	public void setTransactionNumber(Payment obj, int index) {
        String transactionNumber = "transactionNumber_" + index;
        if (transactionNumber.length() > 50) {
            transactionNumber = transactionNumber.substring(0, 50);
        }
        obj.setTransactionNumber(transactionNumber);
    }

	public Payment getSpecificPayment(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Payment obj = data.get(index);
        Long id = obj.getId();
        return Payment.findPayment(id);
    }

	public Payment getRandomPayment() {
        init();
        Payment obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        obj = Payment.findPayment(id);
        obj.getPrograms();
        return obj;
    }

	public boolean modifyPayment(Payment obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Payment.findPaymentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Payment' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Payment>();
        for (int i = 0; i < 10; i++) {
            Payment obj = getNewTransientPayment(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
