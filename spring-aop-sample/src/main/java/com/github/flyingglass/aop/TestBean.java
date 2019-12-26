package com.github.flyingglass.aop;

import com.github.flyingglass.aop.custom.AopTest;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * @author: fly
 * Created date: 2019/12/26 15:02
 */
@Component
@Data
@Accessors(chain = true)
@AopTest("test")
public class TestBean {

    private String tag = "Test!";
}
