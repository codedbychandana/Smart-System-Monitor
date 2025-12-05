package com.smart_system_monitor.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    
    @Pointcut("execution(public * com.smart_system_monitor..*(..))")
    private void loggingPointcutSignature(){}
}
