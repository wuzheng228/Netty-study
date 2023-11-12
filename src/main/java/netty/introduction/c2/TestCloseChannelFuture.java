package netty.introduction.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class TestCloseChannelFuture {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        Channel channel = channelFuture.sync().channel();

        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
//                    ChannelFuture future = channel.closeFuture();
//                    try {
//                        future.sync();
//                        log.debug("处理channel已关闭，释放group资源");
//                        group.shutdownGracefully();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                    break;
                }
                channel.writeAndFlush(s);
            }
        }, "intput").start();

        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            log.debug("处理channel已关闭，释放group资源");
            group.shutdownGracefully();
        });
    }
}
