package com.github.flyingglass.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.netty.buffer.Unpooled.EMPTY_BUFFER;
import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author ly
 * @date 2020/7/23 9:42
 */
@Slf4j
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            log.error("请传入端口");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws InterruptedException {
        EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }


    @ChannelHandler.Sharable
    static public class EchoServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            log.info("Server Received:" + in.toString(UTF_8));
            ctx.write(in);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.info(cause.getMessage(), cause);
            ctx.close();
        }
    }
}
