package com.wzfry.client;

import com.wzfry.message.RpcRequestMessage;
import com.wzfry.protocol.MessageCodec;
import com.wzfry.protocol.MessageCodecShareable;
import com.wzfry.protocol.ProtocolFrameDecoder;
import com.wzfry.server.handler.RpcResponseMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecShareable MESSAGE_CODEC = new MessageCodecShareable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(RPC_HANDLER);
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            // 发送Rpc请求
            ChannelFuture future = channel.writeAndFlush(new RpcRequestMessage(
                    1,
                    "com.wzfry.server.service.HelloService",
                    "sayHello",
                    String.class,
                    new Class[]{String.class},
                    new Object[]{"张三"}
            )).addListener(promise -> {
                if (!promise.isSuccess()) {
                    Throwable cause = promise.cause();
                    log.error("error", cause);
                }
            });

            future.addListener(promise->{
                if (promise.isSuccess()) {
                    log.debug("resp {}", promise.getNow());
                } else {
                    log.error("", promise.cause());
                }
            });

            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
