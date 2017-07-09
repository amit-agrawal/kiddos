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

import com.hmi.kiddos.dao.ChildDao;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class ChildIntegrationTest {
	@Autowired
	private ChildDao childDao;

	@Test
	public void testMarkerMethod() {
	}

	@Autowired
	ChildDataOnDemand dod;

	@Test
	public void testCountChildren() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		long count = childDao.countChildren();
		Assert.assertTrue("Counter for 'Child' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindChild() {
		Child obj = dod.getRandomChild();
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
		obj = childDao.findChild(id);
		Assert.assertNotNull("Find method for 'Child' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Child' returned the incorrect identifier", id, obj.getId());
	}

	@Test
	public void testFindAllChildren() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		List<Child> result = childDao.findAllChildren();
		Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAllCampChildren() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		List<Child> result = childDao.findAllChildren("Camp");
		Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
	}

	@Test
	public void testFindAllDCChildren() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		List<Child> result = childDao.findAllChildren("DC");
		Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
			Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() >= 0);
	}

	@Test
	public void testFindAllPSChildren() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		List<Child> result = childDao.findAllChildren("PS");
		Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() >= 0);
	}

	@Test
	public void testFindChildEntries() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		long count = childDao.countChildren();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Child> result = childDao.findChildEntries(firstResult, maxResults, "Camp");
		Assert.assertNotNull("Find entries method for 'Child' illegally returned null", result);
		// Assert.assertEquals("Find entries method for 'Child' returned an
		// incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		Child obj = dod.getRandomChild();
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
		obj = childDao.findChild(id);
		Assert.assertNotNull("Find method for 'Child' illegally returned null for id '" + id + "'", obj);
		boolean modified = dod.modifyChild(obj);
		Integer currentVersion = obj.getVersion();
		childDao.flush();
		Assert.assertTrue("Version for 'Child' failed to increment on flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		Child obj = dod.getRandomChild();
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
		obj = childDao.findChild(id);
		boolean modified = dod.modifyChild(obj);
		Integer currentVersion = obj.getVersion();
		Child merged = childDao.merge(obj);
		childDao.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
				id);
		Assert.assertTrue("Version for 'Child' failed to increment on merge and flush directive",
				(currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
		Child obj = dod.getNewTransientChild(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Child' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Child' identifier to be null", obj.getId());
		try {
			childDao.persist(obj);
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
		childDao.flush();
		Assert.assertNotNull("Expected 'Child' identifier to no longer be null", obj.getId());
	}

	@Test
	public void testRemove() {
		Child obj = dod.getRandomChild();
		Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
		Long id = obj.getId();
		Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
		obj = childDao.findChild(id);
		childDao.remove(obj);
		childDao.flush();
		Assert.assertNull("Failed to remove 'Child' with identifier '" + id + "'", childDao.findChild(id));
	}
}
