package com.github.flyingglass.aop.custom;

import com.github.flyingglass.aop.custom.AnnotationTestExecutionInterceptor;
import com.github.flyingglass.aop.custom.AopTest;
import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author: fly
 * Created date: 2019/12/26 15:41
 */
public class TestAnnotationAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;
    private Pointcut pointcut;

    public TestAnnotationAdvisor(@NonNull AnnotationTestExecutionInterceptor interceptor) {
        this.advice = interceptor;
        this.pointcut = buildPointcut();
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    private Pointcut buildPointcut() {

        Pointcut cpc = new AnnotationMatchingPointcut(AopTest.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(AopTest.class);
        return new ComposablePointcut(cpc).union(mpc);
    }
}
