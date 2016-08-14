package com.hmi.kiddos.model;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
	@Ignore
    public void sendMail() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        sendGmail();
        
        long count = childDao.countChildren();
        Assert.assertTrue("Counter for 'Child' incorrectly reported there were no entries", count > 0);
    }

	private void sendGmail() {
		final String username = "amit01@gmail.com";
		final String password = "no12blame";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("amit01@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("amit01@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}		
	}

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
        long count = childDao.countChildren();
        Assert.assertTrue("Too expensive to perform a find all test for 'Child', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Child> result = childDao.findAllChildren();
        Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAllSCChildren() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        long count = childDao.countChildren();
        Assert.assertTrue("Too expensive to perform a find all test for 'Child', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Child> result = childDao.findAllChildren("SC");
        Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAllDCChildren() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        long count = childDao.countChildren();
        Assert.assertTrue("Too expensive to perform a find all test for 'Child', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Child> result = childDao.findAllChildren("DC");
        Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAllPSChildren() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        long count = childDao.countChildren();
        Assert.assertTrue("Too expensive to perform a find all test for 'Child', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Child> result = childDao.findAllChildren("PS");
        Assert.assertNotNull("Find all method for 'Child' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Child' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindChildEntries() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        long count = childDao.countChildren();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Child> result = childDao.findChildEntries(firstResult, maxResults, "SC");
        Assert.assertNotNull("Find entries method for 'Child' illegally returned null", result);
        //Assert.assertEquals("Find entries method for 'Child' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Child obj = dod.getRandomChild();
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
        obj = childDao.findChild(id);
        Assert.assertNotNull("Find method for 'Child' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyChild(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Child' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Child obj = dod.getRandomChild();
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
        obj = childDao.findChild(id);
        boolean modified =  dod.modifyChild(obj);
        Integer currentVersion = obj.getVersion();
        Child merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Child' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", dod.getRandomChild());
        Child obj = dod.getNewTransientChild(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Child' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Child' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Child' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Child obj = dod.getRandomChild();
        Assert.assertNotNull("Data on demand for 'Child' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Child' failed to provide an identifier", id);
        obj = childDao.findChild(id);
        obj.remove(new ChildDao());
        obj.flush();
        Assert.assertNull("Failed to remove 'Child' with identifier '" + id + "'", childDao.findChild(id));
    }
}
