package com.github.flyingglass.netty.rpc.provider;

import com.github.flyingglass.netty.rpc.netty.NettyServer;

/**
 * @author: fly
 * Created date: 2019/12/27 15:56
 */
public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("localhost", 8088);
    }
}
