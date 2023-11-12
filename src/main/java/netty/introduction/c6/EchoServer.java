package netty.introduction.c6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(2))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = msg instanceof ByteBuf ? (ByteBuf) msg : null;
                                if (buffer != null) {
                                    System.out.println(Charset.defaultCharset().decode(buffer.nioBuffer()).toString());
                                    ByteBuf response = ByteBufAllocator.DEFAULT.buffer();
                                    response.writeBytes(buffer);
                                    ctx.writeAndFlush(buffer);
                                    // 思考需要释放buffer吗
                                    // 需要释放buffer应为 没有继续向下传递交给尾部处理器处理
                                    buffer.release();
                                    // 思考需要释放responese吗
                                    // 不用释放responese 英文 ctx.writeAndFlush 源码当中最后会交给头部处理器处理
                                }
                            }
                        });
                    }
                }).channel(NioServerSocketChannel.class)
                .bind(8080);
    }
}
