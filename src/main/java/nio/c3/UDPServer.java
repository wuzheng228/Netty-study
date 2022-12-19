package nio.c3;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class UDPServer {
    public static void main(String[] args) throws IOException {
        // 开启udp channel
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8081));
        System.out.println("waiting.....");

        // 绑定Selector
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(32));

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isReadable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    DatagramChannel dc = (DatagramChannel) key.channel();
                    dc.receive(buffer);
                    ByteBufferUtil.debugAll(buffer);
                }
            }
        }
    }
}
