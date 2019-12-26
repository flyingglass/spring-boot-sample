package com.github.flyingglass.aop.config;

import com.github.flyingglass.aop.custom.AnnotationTestExecutionInterceptor;
import com.github.flyingglass.aop.custom.TestAnnotationAdvisor;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fly
 * Created date: 2019/12/26 15:52
 */
@Configuration
public class TestAopConfiguration {


//    @Bean
//    public TestAnnotationAdvisor testAnnotationAdvisor() {
//        AnnotationTestExecutionInterceptor interceptor = new AnnotationTestExecutionInterceptor();
//
//        TestAnnotationAdvisor advisor = new TestAnnotationAdvisor(interceptor);
//
//
//        return advisor;
//    }

    /**
     * BPP连接AOP <-> IOC
     * @return
     */
    @Bean
    public BeanPostProcessor advisorPostProcessor() {
        return new AbstractBeanFactoryAwareAdvisingPostProcessor() {
            @Override
            public void setBeanFactory(BeanFactory beanFactory) {
                super.setBeanFactory(beanFactory);

                AnnotationTestExecutionInterceptor interceptor = new AnnotationTestExecutionInterceptor();

                advisor = new TestAnnotationAdvisor(interceptor);

            }
        };
    }

}
