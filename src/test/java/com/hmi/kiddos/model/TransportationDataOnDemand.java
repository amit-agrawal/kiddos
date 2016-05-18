package com.hmi.kiddos.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.annotations.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = Transportation.class)
public class TransportationDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Transportation> data;

	public Transportation getNewTransientTransportation(int index) {
        Transportation obj = new Transportation();
        setDriverName(obj, index);
        setNotes(obj, index);
        setVan(obj, index);
        return obj;
    }

	public void setDriverName(Transportation obj, int index) {
        String driverName = "driverName_" + index;
        if (driverName.length() > 80) {
            driverName = driverName.substring(0, 80);
        }
        obj.setDriverName(driverName);
    }

	public void setNotes(Transportation obj, int index) {
        String notes = "notes_" + index;
        if (notes.length() > 400) {
            notes = notes.substring(0, 400);
        }
        obj.setNotes(notes);
    }

	public void setVan(Transportation obj, int index) {
        String van = "van_" + index;
        if (van.length() > 80) {
            van = van.substring(0, 80);
        }
        obj.setVan(van);
    }

	public Transportation getSpecificTransportation(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Transportation obj = data.get(index);
        Long id = obj.getId();
        return Transportation.findTransportation(id);
    }

	public Transportation getRandomTransportation() {
        init();
        Transportation obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Transportation.findTransportation(id);
    }

	public boolean modifyTransportation(Transportation obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Transportation.findTransportationEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Transportation' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Transportation>();
        for (int i = 0; i < 10; i++) {
            Transportation obj = getNewTransientTransportation(i);
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
