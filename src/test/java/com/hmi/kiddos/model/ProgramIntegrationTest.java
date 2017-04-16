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

import com.hmi.kiddos.dao.ProgramDao;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class ProgramIntegrationTest {
	@Autowired
	private ProgramDao programDao;

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	ProgramDataOnDemand dod;

	@Test
	public void testCountPrograms() {
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", dod.getRandomProgram());
		long count = programDao.countPrograms();
		Assert.assertTrue("Counter for 'Program' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindProgram() {
		Program obj = dod.getRandomProgram();
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Program' failed to provide an identifier", id);
		obj = programDao.findProgram(id);
		Assert.assertNotNull("Find method for 'Program' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Program' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllPrograms() {
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", dod.getRandomProgram());
		long count = programDao.countPrograms();
		Assert.assertTrue(
				"Too expensive to perform a find all test for 'Program', as there are " + count
						+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 2500);
		List<Program> result = programDao.findAllPrograms();
		Assert.assertNotNull("Find all method for 'Program' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Program' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAllActivePrograms() {
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", dod.getRandomProgram());
		long count = programDao.countPrograms();
		Assert.assertTrue(
				"Too expensive to perform a find all test for 'Program', as there are " + count
						+ " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
				count < 2500);
		List<Program> result = programDao.findCurrentFuturePrograms();
		Assert.assertNotNull("Find all method for 'Program' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Program' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindProgramEntries() {
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", dod.getRandomProgram());
		long count = programDao.countPrograms();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Program> result = programDao.findProgramEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'Program' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'Program' returned an incorrect number of entries", count,
				result.size());
	}

	@Test
	public void testFlush() {
		Program obj = dod.getRandomProgram();
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Program' failed to provide an identifier", id);
		obj = programDao.findProgram(id);
		Assert.assertNotNull("Find method for 'Program' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyProgram(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'Program' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		Program obj = dod.getRandomProgram();
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Program' failed to provide an identifier", id);
		obj = programDao.findProgram(id);
		boolean modified = dod.modifyProgram(obj);
		Integer currentVersion = obj.getVersion();
		Program merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
				id);
		Assert.assertTrue("Version for 'Program' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", dod.getRandomProgram());
		Program obj = dod.getNewTransientProgram(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Program' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Program' identifier to be null", obj.getId());
		try {
			obj.persist();
		} catch (final ConstraintViolationException e) {
			final StringBuilder msg = new StringBuilder();
			for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
				final ConstraintViolation<?> cv = iter.next();
				msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath())
						.append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue())
						.append(")").append("]");
			}
			throw new IllegalStateException(msg.toString(), e);
		}
		obj.flush();
		Assert.assertNotNull("Expected 'Program' identifier to no longer be null", obj.getId());
	}

	@Test
	@Ignore
	public void testRemove() {
		Program obj = dod.getRandomProgram();
		Assert.assertNotNull("Data on demand for 'Program' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Program' failed to provide an identifier", id);
		obj = programDao.findProgram(id);
		obj.remove(programDao);
		obj.flush();
		Assert.assertNull("Failed to remove 'Program' with identifier '" + id + "'", programDao.findProgram(id));
	}
}
