package com.github.flyingglass.aop.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: fly
 * Created date: 2019/12/26 14:58
 */
@Component
@Slf4j
@Aspect
public class LogAspect {

//    @Pointcut("execution(* com.github.flyingglass.sample.aop..*.*(..))")
    @Pointcut("execution(* com.github.flyingglass.aop.TestBean.*(..))")
    private void allMethod() {

    }


    @Before("allMethod()")
    void before(JoinPoint call) {
        String className = call.getTarget().getClass().getName();
        String methodName = call.getSignature().getName();

        log.info(className + ", " + methodName + " before...");
    }

    @After("allMethod()")
    void after(JoinPoint call) {
        String className = call.getTarget().getClass().getName();
        String methodName = call.getSignature().getName();

        log.info(className + ", " + methodName + " after...");
    }


}
