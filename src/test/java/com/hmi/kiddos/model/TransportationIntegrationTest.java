package com.hmi.kiddos.model;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class TransportationIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    TransportationDataOnDemand dod;

	@Test
    public void testCountTransportations() {
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", dod.getRandomTransportation());
        long count = Transportation.countTransportations();
        Assert.assertTrue("Counter for 'Transportation' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindTransportation() {
        Transportation obj = dod.getRandomTransportation();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to provide an identifier", id);
        obj = Transportation.findTransportation(id);
        Assert.assertNotNull("Find method for 'Transportation' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Transportation' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllDropTransportations() {
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", dod.getRandomTransportation());
        long count = Transportation.countTransportations();
        Assert.assertTrue("Too expensive to perform a find all test for 'Transportation', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Transportation> result = Transportation.findAllDropTransportations();
        Assert.assertNotNull("Find all method for 'Transportation' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Transportation' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAllTransportations() {
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", dod.getRandomTransportation());
        long count = Transportation.countTransportations();
        Assert.assertTrue("Too expensive to perform a find all test for 'Transportation', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Transportation> result = Transportation.findAllTransportations();
        Assert.assertNotNull("Find all method for 'Transportation' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Transportation' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindTransportationEntries() {
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", dod.getRandomTransportation());
        long count = Transportation.countTransportations();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Transportation> result = Transportation.findTransportationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Transportation' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Transportation' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Transportation obj = dod.getRandomTransportation();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to provide an identifier", id);
        obj = Transportation.findTransportation(id);
        Assert.assertNotNull("Find method for 'Transportation' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTransportation(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Transportation' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Transportation obj = dod.getRandomTransportation();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to provide an identifier", id);
        obj = Transportation.findTransportation(id);
        boolean modified =  dod.modifyTransportation(obj);
        Integer currentVersion = obj.getVersion();
        Transportation merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Transportation' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", dod.getRandomTransportation());
        Transportation obj = dod.getNewTransientTransportation(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Transportation' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Transportation' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Transportation' identifier to no longer be null", obj.getId());
    }

	@Test
	@Ignore
    public void testRemove() {
        Transportation obj = dod.getRandomTransportation();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Transportation' failed to provide an identifier", id);
        obj = Transportation.findTransportation(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Transportation' with identifier '" + id + "'", Transportation.findTransportation(id));
    }
}
