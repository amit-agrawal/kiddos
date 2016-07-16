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
		long start = System.currentTimeMillis();
		Object result = point.proceed();
		long timeTaken = System.currentTimeMillis() - start;
		
		if (timeTaken > 1)
			Logger.getLogger(LoggingAspect.class).info(String.format("Called %s.%s(%s) in %d ms", point.getSignature().getDeclaringType().getSimpleName(), 
				MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
				point.getArgs(), timeTaken));
		return result;
	}
	
}
