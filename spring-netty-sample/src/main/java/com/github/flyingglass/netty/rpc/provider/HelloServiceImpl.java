package com.github.flyingglass.netty.rpc.provider;

import com.github.flyingglass.netty.rpc.pinterface.HelloService;

/**
 * @author: fly
 * Created date: 2019/12/27 12:05
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return msg != null ? msg + " ------> I am ok." : "I am ok.";
    }
}
