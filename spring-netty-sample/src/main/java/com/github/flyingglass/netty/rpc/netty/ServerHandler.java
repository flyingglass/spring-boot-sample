package com.github.flyingglass.netty.rpc.netty;

import com.github.flyingglass.netty.rpc.consumer.ClientBootstrap;
import com.github.flyingglass.netty.rpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: fly
 * Created date: 2019/12/27 12:09
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.toString().startsWith(ClientBootstrap.providerName)) {
            String result = new HelloServiceImpl()
                    .hello(msg.toString() .substring(msg.toString().lastIndexOf("#")+1));
            ctx.writeAndFlush(result);
        }
    }
}
