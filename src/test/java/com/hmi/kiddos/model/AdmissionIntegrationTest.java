package com.hmi.kiddos.model;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hmi.kiddos.dao.AdmissionDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class AdmissionIntegrationTest {
	@Autowired
	private AdmissionDao admissionDao;

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    AdmissionDataOnDemand dod;

	@Test
    public void testCountAdmissions() {
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", dod.getRandomAdmission());
        long count = admissionDao.countAdmissions();
        Assert.assertTrue("Counter for 'Admission' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindAdmission() {
        Admission obj = dod.getRandomAdmission();
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Admission' failed to provide an identifier", id);
        obj = admissionDao.findAdmission(id);
        Assert.assertNotNull("Find method for 'Admission' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Admission' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllAdmissions() {
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", dod.getRandomAdmission());
        long count = admissionDao.countAdmissions();
        Assert.assertTrue("Too expensive to perform a find all test for 'Admission', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 2000);
        List<Admission> result = admissionDao.findAllAdmissions();
        Assert.assertNotNull("Find all method for 'Admission' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Admission' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAdmissionEntries() {
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", dod.getRandomAdmission());
        long count = admissionDao.countAdmissions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Admission> result = admissionDao.findAdmissionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Admission' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Admission' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Admission obj = dod.getRandomAdmission();
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Admission' failed to provide an identifier", id);
        obj = admissionDao.findAdmission(id);
        Assert.assertNotNull("Find method for 'Admission' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAdmission(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Admission' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Admission obj = dod.getRandomAdmission();
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Admission' failed to provide an identifier", id);
        obj = admissionDao.findAdmission(id);
        boolean modified =  dod.modifyAdmission(obj);
        Integer currentVersion = obj.getVersion();
        Admission merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Admission' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", dod.getRandomAdmission());
        Admission obj = dod.getNewTransientAdmission(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Admission' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Admission' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Admission' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Admission obj = dod.getRandomAdmission();
        Assert.assertNotNull("Data on demand for 'Admission' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Admission' failed to provide an identifier", id);
        obj = admissionDao.findAdmission(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Admission' with identifier '" + id + "'", admissionDao.findAdmission(id));
    }
}
