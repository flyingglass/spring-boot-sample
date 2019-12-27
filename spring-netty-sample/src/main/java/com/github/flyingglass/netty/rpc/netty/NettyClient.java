package com.github.flyingglass.netty.rpc.netty;

import com.github.flyingglass.netty.rpc.netty.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: fly
 * Created date: 2019/12/27 15:24
 */
public class NettyClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static ClientHandler clientHandler;


    public Object createProxy(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},
                (proxy, method, args) -> {
                    if(null == clientHandler) {
                        initClient();
                    }
                    clientHandler.setPara(providerName + args[0]);
                    return executorService.submit(clientHandler).get();
                }
        );
    }



    private static void initClient(){
        clientHandler = new ClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(clientHandler);
                    }
                });
        try {
            b.connect("localhost", 8088).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
