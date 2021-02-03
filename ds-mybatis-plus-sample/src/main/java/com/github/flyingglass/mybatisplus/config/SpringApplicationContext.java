package com.github.flyingglass.mybatisplus.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fly
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext ctx = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        return (T) ctx.getBean(beanId);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return (T) ctx.getBean(requiredType);
    }
}
