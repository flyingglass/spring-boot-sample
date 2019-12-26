package com.github.flyingglass.aop.custom;

import java.lang.annotation.*;

/**
 * @author: fly
 * Created date: 2019/12/26 15:28
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AopTest {

    String value();
}
