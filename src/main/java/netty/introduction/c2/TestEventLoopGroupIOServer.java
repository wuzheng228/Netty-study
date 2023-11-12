package netty.introduction.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEventLoopGroupIOServer {
    public static void main(String[] args) throws Exception {
        DefaultEventLoopGroup normalWorkers = new DefaultEventLoopGroup(2);
        new ServerBootstrap()
                .group(new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(normalWorkers, "myHandler", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                                ByteBuf byteBuf = msg instanceof ByteBuf ? (ByteBuf)msg : null;
                                if (byteBuf != null) {
                                    byte[] bytes = new byte[21];
                                    ByteBuf len = byteBuf.readBytes(bytes, 0, byteBuf.readableBytes());
                                    log.debug(new String(bytes));
                                }
                            }
                        });
                    }
                })
                .bind(8080).sync();
    }
}
