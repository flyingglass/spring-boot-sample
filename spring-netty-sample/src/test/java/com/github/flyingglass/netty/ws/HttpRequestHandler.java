package com.github.flyingglass.netty.ws;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * @author ly
 * @date 2020/7/23 19:52
 */
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "Unable to locate index.html", e
            );
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // 如果请求了WebSocket协议升级，则增加引用计数(调用retain()方法），并将它传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(msg.uri())) {
            ctx.fireChannelRead(msg.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(msg)) {
                send100Continue(ctx);
            }
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                    msg.protocolVersion(), HttpResponseStatus.OK
            );
            response.headers().set(
                    CONTENT_TYPE,
                    "text/html; charset=UTF-8"
            );
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            if (keepAlive) {
                response.headers().set(
                        CONTENT_LENGTH, file.length()
                );
                response.headers().set(
                        CONNECTION, KEEP_ALIVE
                );
            }
            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write( new DefaultFileRegion( file.getChannel(), 0, file.length() ) );
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            ChannelFuture future = ctx.writeAndFlush( LastHttpContent.EMPTY_LAST_CONTENT );
            // 如果没有keep-alive，写操作完成后关闭Channel
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE
        );
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info(cause.getMessage(), cause);
        ctx.close();
    }
}
