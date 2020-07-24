package com.github.flyingglass.netty.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author ly
 * @date 2020/7/24 16:02
 */
@Slf4j
public class ChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChatServerInitializer(channelGroup)
                );
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        acceptorGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Please give post as argument.");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        final ChatServer endpoint = new ChatServer();
        ChannelFuture future = endpoint.start(
                new InetSocketAddress(port)
        );
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> endpoint.destroy())
        );
        future.channel().closeFuture().syncUninterruptibly();
    }
}
