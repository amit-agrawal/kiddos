package com.hmi.kiddos.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.hmi.kiddos.model.Payment;

@Aspect
@Configurable
public class MailingAspect {
	@Autowired
	private MailUtil mailUtil;

	@Autowired
	private DocumentGenerator docGenerator;

	@Around("execution(* com.hmi.kiddos.controllers..*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result = point.proceed();

		try {
			String methodName = MethodSignature.class.cast(point.getSignature()).getMethod().getName();
			if (methodName.equals("create") || methodName.equals("update") || methodName.equals("delete")) {
				Logger.getLogger(MailingAspect.class).debug("Sending mail for method: " + methodName);
				Object[] args = point.getArgs();
				String className = point.getSignature().getDeclaringType().getSimpleName();

				String objectString = args[0].toString();

				/*
				 * if (methodName.equals("delete")) { Long deletedId = (Long)
				 * args[0];
				 * 
				 * }
				 */
				String docPath = null;
				if (args[0] instanceof Payment && (methodName.equals("create") || methodName.equals("update"))) {
					Payment payment = (Payment) args[0];
					docPath = docGenerator.generateInvoice(payment);
				}
				mailUtil.sendGmail(className, methodName, objectString, docPath);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			Logger.getLogger(MailingAspect.class).error("Exception while mailing", t);
		}
		return result;
	}
}
