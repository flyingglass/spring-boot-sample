package com.github.flyingglass.aop.custom;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author: fly
 * Created date: 2019/12/26 15:40
 */
@Slf4j
public class AnnotationTestExecutionInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        log.info(getKey(invocation));

        return invocation.proceed();
    }


    private String getKey(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> declaringClass = invocation.getMethod().getDeclaringClass();

        log.info("{}", method.isAnnotationPresent(AopTest.class));

        AopTest aopTest = method.isAnnotationPresent(AopTest.class) ? method.getAnnotation(AopTest.class)
                : AnnotationUtils.findAnnotation(declaringClass, AopTest.class);

        return aopTest.value();
    }
}
