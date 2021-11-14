package com.bitoasis.ticker.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Configuration
@Log4j2
public class LoggingAspect {
    //Logging map keys
    private static final String STATE = "State";
    private static final String EXECUTION_TIME = "ExecutionTime";
    private static final String RETURN_VALUE = "ReturnValue";
    private static final String CLASS_NAME = "ClassName";
    private static final String METHOD_NAME = "MethodName";
    private static final String THREAD = "Thread";
    private static final String ARGUMENT = "Argument-";

    //Logging map values
    private static final String STARTED = "Started";
    private static final String COMPLETED = "Completed";
    private static final String FAILED = "Failed";
    private static final String MILLIS = "ms";
    private static final String NULL = "null";


    //Pointcut that matches all repositories, services and Web REST endpoints.
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    //Point Cut to exclude Scheduled trigger
    @Pointcut("execution (* com.bitoasis.ticker.service.TickerService.scheduledTrigger(..))")
    public void excludePointcut() {
    }

    //Pointcut that matches all Spring beans in the application's main packages.
    @Pointcut("within(com.bitoasis.ticker..*)" +
            " || within(com.bitoasis.ticker.service..*)" +
            " || within(com.bitoasis.ticker.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    @Around("applicationPackagePointcut() && springBeanPointcut() && ! excludePointcut()")
    public Object logAroundMethodCall(ProceedingJoinPoint pjp) throws Throwable {
        //log.info("--------------------------------------------START-----------------------------------------------------------");
        long startTime = 0l;
        long endTime = 0l;
        Map<String, Object> logEntries = getLogEntries(pjp);
        try {
            startTime = System.currentTimeMillis();
            logBefore(logEntries);
            Object result = pjp.proceed(pjp.getArgs());
            endTime = System.currentTimeMillis();
            logAfter(logEntries, startTime, endTime, result);
            // log.info("----------------------------------------END---------------------------------------------------------------");
            return result;
        } catch (Exception e) {
            logException(logEntries, startTime, endTime, e);
            throw e;
        }
    }


    private void logBefore(Map<String, Object> logEntries) {
        logEntries.put(STATE, STARTED);
        log.info("START - " + LoggingHelperV2.logRegular(logEntries));
    }

    private void logAfter(Map<String, Object> logEntries, long startTime, long endTime, Object result) {
        logEntries.put(STATE, COMPLETED);
        logEntries.put(EXECUTION_TIME, (endTime - startTime) + MILLIS);
        if (result != null) {
            logEntries.put(RETURN_VALUE, result);
        } else {
            logEntries.put(RETURN_VALUE, NULL);
        }
        log.info("END - " + LoggingHelperV2.logRegular(logEntries));
        // log.info("\n-------------------------------------------------------------------------------------------------------");
    }

    private void logException(Map<String, Object> logEntries, long startTime, long endTime, Exception e) {
        logEntries.put(STATE, FAILED);
        logEntries.put(EXECUTION_TIME, (endTime - startTime) + MILLIS);

        log.error("EXCEPTION - " + LoggingHelperV2.logRegular(logEntries, e));
    }

    private Map<String, Object> getLogEntries(JoinPoint joinPoint) {
        Map<String, Object> logEntries = new LinkedHashMap<>();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logEntries.put(CLASS_NAME, className);
        String methodName = joinPoint.getSignature().getName();
        logEntries.put(METHOD_NAME, methodName);
        logEntries.put(THREAD, Thread.currentThread().getName());

        Object[] argVals = joinPoint.getArgs();
        if (argVals != null) {
            int i = 1;
            for (Object argVal : argVals) {
                if (argVal != null) {
                    logEntries.put(ARGUMENT + i, argVal);
                }
                i++;
            }
        }
        return logEntries;
    }
}
