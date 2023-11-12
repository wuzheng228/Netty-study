package netty.introduction.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class TestNettyServer {
    public static void main(String[] args) throws Exception {
        ChannelFuture sync = new ServerBootstrap()
                .group(new NioEventLoopGroup()) // 创建NioEventGroup 线程池 + selector
                .channel(NioServerSocketChannel.class) //选择socket 的实现类
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // 为socketchannel 添加处理器
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()); // ByteBuf 解码为 String
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 业务处理器 处理上一个处理器的结果
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(8080).sync();
        sync.get();
        System.out.println("main thread ended");
    }
}
