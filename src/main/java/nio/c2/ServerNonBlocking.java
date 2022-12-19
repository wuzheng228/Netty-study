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
public class ServerNonBlocking {
    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {

            ssc.bind(new InetSocketAddress(8080));
            ssc.configureBlocking(false);
            boolean blocking = ssc.isBlocking();
            System.out.println(blocking);
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
//                log.info("connecting.......");
                SocketChannel channel = ssc.accept(); // 非阻塞 没有建立连接时返回null
                if (channel != null) {
                    log.info("connected");
                    channel.configureBlocking(false);
                    channels.add(channel);
                }

                for (SocketChannel socketChannel : channels) {
//                    log.info("before read ...{}", socketChannel);
                    int read = socketChannel.read(buffer);// 非阻塞 无 数据时 返回 0
                    if(read > 0) {
                        buffer.flip();
                        ByteBufferUtil.debugRead(buffer);
                        buffer.clear();
                        log.debug("after read ... {}", socketChannel);
                    }
                }
            }
        }
    }
}
