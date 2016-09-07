package com.hmi.kiddos.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LoggingAspect {
	@Around("execution(* com.hmi..*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		long start = System.currentTimeMillis();
		result = point.proceed();
		long timeTaken = System.currentTimeMillis() - start;
		try {

			if (timeTaken > 2) {
				//String args = Arrays.asList(point.getArgs()).toString();
				//String args = point.getArgs().toString();
				Logger.getLogger(LoggingAspect.class).info(String.format("Called %s.%s in %d ms",
						point.getSignature().getDeclaringType().getSimpleName(),
						MethodSignature.class.cast(point.getSignature()).getMethod().getName(), timeTaken));
			}
		} catch (Throwable t) {
			t.printStackTrace();
			Logger.getLogger(LoggingAspect.class).error("Exception while logging", t);
		}
		return result;
	}

}
