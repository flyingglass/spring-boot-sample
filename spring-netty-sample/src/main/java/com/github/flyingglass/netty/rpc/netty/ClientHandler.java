package com.github.flyingglass.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author: fly
 * Created date: 2019/12/27 12:11
 */
public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String para;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    synchronized public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    synchronized public Object call() throws Exception {
        context.writeAndFlush(para);
        wait();
        return result;
    }

    void setPara(String para) {
        this.para = para;
    }
}
