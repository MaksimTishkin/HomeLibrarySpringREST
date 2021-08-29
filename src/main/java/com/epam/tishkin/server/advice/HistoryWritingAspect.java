package com.epam.tishkin.server.advice;

import com.epam.tishkin.server.util.HistoryManagerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Aspect
public class HistoryWritingAspect {
    private final HistoryManagerUtil historyManager;

    @Autowired
    public HistoryWritingAspect(HistoryManagerUtil historyManager) {
        this.historyManager = historyManager;
    }

    @Pointcut("execution(* com.epam.tishkin.server.service.impl.*.*(..)) && !within(* ..UserServiceImpl)")
    public void entityActionMethods() {}

    @AfterReturning(value = "entityActionMethods()", returning = "response")
    public void logGetMethods(JoinPoint joinPoint, Object response) {
        historyManager.write(joinPoint.getSignature().getName()
                + " args: " + Arrays.toString(joinPoint.getArgs())
                + " response: " + response.toString());
    }
}
