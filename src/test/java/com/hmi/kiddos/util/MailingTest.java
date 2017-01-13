package com.hmi.kiddos.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Configurable
public class MailingTest {
	@Autowired
	private MailUtil mailUtil;

	@Test
	@Ignore
	public void sendGmailTest() {
		mailUtil.sendGmail("AdmissionController", "create", "1,2");
	}
}
