package nio.c2;


import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerSelector {
    public static void main(String[] args) throws Exception {

        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.bind(new InetSocketAddress(8080));
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int count = selector.select(); // 阻塞当前线程 等待注册channel的事件发生 count 时 注册的key的数量
                log.debug("select count: {}", count);

                // 获取select 注册的所有的 key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        ServerSocketChannel c = (ServerSocketChannel) key.channel();
                        // 事件必须处理否则下次还会触发
                        SocketChannel sc = c.accept();
                        log.debug("{}",sc);
                        sc.configureBlocking(false);
                        ByteBuffer buf = ByteBuffer.allocate(16); // buffer 不能共用，所以一个channel分配一个buf
                        sc.register(selector, SelectionKey.OP_READ, buf);
                    } else if (key.isReadable()) {
                        try {
                            SocketChannel c = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            int read = c.read(buffer);
                            if (read == -1) { // 客户端正常关闭时 返回 -1
                                log.debug("end of the stream key cancel");
                                key.cancel();
                                c.close();
                            } else {
                                split(buffer);// 按照/n分隔符打印数据, 如果发生数据截断，会再触发一次读事件，这时候需要对buffer扩容
                                if (buffer.position() == buffer.limit()) {
                                    ByteBuffer newByteBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                    buffer.flip();
                                    newByteBuffer.put(buffer);
                                    key.attach(newByteBuffer);
                                }
                            }
                        } catch (IOException e) {
                            // 防止客户端不正常关闭
                            key.cancel(); // 取消注册在 selector 上的事件 并且 将该key 从 key set 中移除
                        }
                    }
                    // 处理完毕必须将事件移除
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void split(ByteBuffer buffer) {
        buffer.flip();
        ByteBufferUtil.debugRead(buffer);
        int oldLimit = buffer.limit();
        for (int i = 0; i < oldLimit; i++) {
            byte b = buffer.get(i); // 不会改变buffer的 position 指针
            if (b == '\n') {
                System.out.println(buffer.position());
                ByteBuffer target = ByteBuffer.allocate(i - buffer.position() + 1);
                buffer.limit(i + 1); // 右开区间 limit取不到 ， 读的时候 position = limit 时 就不会再读了
                target.put(buffer);// 相当于读buffer position ++, 会改变position 指针
                ByteBufferUtil.debugAll(target);
                buffer.limit(oldLimit);// 还原limit 指针位置, 方便继续读
            }
        }
        buffer.compact();// compact操作解决半包 现象， 未读的字节放到 buffer 首部
    }
}
