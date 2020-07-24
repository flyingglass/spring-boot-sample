package com.github.flyingglass.netty.rpc.consumer;

import com.github.flyingglass.netty.rpc.netty.NettyClient;
import com.github.flyingglass.netty.rpc.pinterface.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fly
 * Created date: 2019/12/27 15:23
 */
@Slf4j
public class ClientBootstrap {

    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        NettyClient consumer = new NettyClient();

        HelloService service = (HelloService) consumer.createProxy(HelloService.class, providerName);

        for (;;) {
            Thread.sleep(1000L);
            log.info(service.hello("are you ok ?"));
        }
    }

}
