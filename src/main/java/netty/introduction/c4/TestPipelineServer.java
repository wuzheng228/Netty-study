package netty.introduction.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class TestPipelineServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(1); // 1
                                ctx.fireChannelRead(msg);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(2); // 2
                                ctx.fireChannelRead(msg);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(3); // 3
//                                ctx.fireChannelRead(msg);
                            }
                        });
//                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
//                            @Override
//                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                System.out.println(4); // 4
//                                ctx.write(msg, promise);
//                            }
//                        });
//                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
//                            @Override
//                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                System.out.println(5); // 5
//                                ctx.write(msg, promise);
//                            }
//                        });
//                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
//                            @Override
//                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                System.out.println(6); // 6
//                                ctx.write(msg, promise);
//                            }
//                        });
                    }
                })
                .bind(new InetSocketAddress("localhost", 8080));
    }
}
