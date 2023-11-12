package com.wzfry.server;

import com.wzfry.message.LoginRequestMessage;
import com.wzfry.message.LoginResponseMessage;
import com.wzfry.protocol.MessageCodecShareable;
import com.wzfry.protocol.ProtocolFrameDecoder;
import com.wzfry.server.handler.*;
import com.wzfry.server.service.UserServiceFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecShareable messageCodecShareable = new MessageCodecShareable();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());// 处理黏包和半包
                    ch.pipeline().addLast(loggingHandler);// 日志
                    ch.pipeline().addLast(messageCodecShareable);// 数据帧 编码解码
                    //5 秒内如果没有收到channel 的数据，会触发一个IdleState#READER_IDLE事件
                    ch.pipeline().addLast(new IdleStateHandler(5,0,0));
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了空读事件
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug("已经5s 没有读到数据了");
                                ctx.channel().close();
                            }
                        }
                    });

                    ch.pipeline().addLast(new LoginRequestMessageHandler());
                    ch.pipeline().addLast(new ChatRequestMessageHandler());
                    ch.pipeline().addLast(new GroupChatRequestMessageHandler());
                    ch.pipeline().addLast(new GroupCreateRequestMessageHandler());
                    ch.pipeline().addLast(new GroupJoinRequestMessageHandler());
                    ch.pipeline().addLast(new GroupMembersMessageHandler());
                    ch.pipeline().addLast(new GroupQuitRequestMessageHandler());
                    ch.pipeline().addLast(new QuitHandler());

                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("chat server err:", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
