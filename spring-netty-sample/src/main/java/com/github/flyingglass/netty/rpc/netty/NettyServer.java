package com.github.flyingglass.netty.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author: fly
 * Created date: 2019/12/27 15:56
 */
public class NettyServer {
    private static final int CPUS = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static void startServer(String host, int port) {
        startServer0(host, port);
    }

    private static void startServer0(String host, int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
        // From https://github.com/netty/netty/wiki/Thread-Affinity
        ThreadFactory threadFactory = new AffinityThreadFactory(
                "ServerAffinityThreadFactory",
                AffinityStrategies.DIFFERENT_CORE
        );
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(
                2 * CPUS, threadFactory
        );

        bootstrap.group(acceptorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(new ServerHandler());
                    }
                });
        try {
            bootstrap.bind(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
