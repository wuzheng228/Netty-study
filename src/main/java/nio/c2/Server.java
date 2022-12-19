package nio.c2;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {

            ssc.bind(new InetSocketAddress(8080));
            boolean blocking = ssc.isBlocking();
            System.out.println(blocking);
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
                log.info("connecting.......");
                SocketChannel channel = ssc.accept(); // 阻塞 线程停止运行
                log.info("connected");
                channels.add(channel);

                for (SocketChannel socketChannel : channels) {
                    log.info("before read ...{}", socketChannel);
                    socketChannel.read(buffer);// 阻塞方法
                    buffer.flip();
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();
                    log.debug("after read ... {}", socketChannel);
                }
            }
        }

    }
}
