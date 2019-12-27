package com.github.flyingglass.netty.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: fly
 * Created date: 2019/12/27 15:56
 */
public class NettyServer {
    public static void startServer(String host, int port) {
        startServer0(host, port);
    }

    private static void startServer0(String host, int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(nioEventLoopGroup)
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
