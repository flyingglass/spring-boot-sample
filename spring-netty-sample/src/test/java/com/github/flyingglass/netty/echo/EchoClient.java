package com.github.flyingglass.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author ly
 * @date 2020/7/23 10:02
 */
@Slf4j
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel> (){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            log.error("Usage: " + EchoClient.class.getSimpleName() + "<host> <port>");
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }

    @ChannelHandler.Sharable
    static public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", UTF_8));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            log.info("Client received: " + msg.toString(UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.info(cause.getMessage(), cause);
            ctx.close();
        }
    }
}
