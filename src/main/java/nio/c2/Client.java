package nio.c2;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
            boolean connected = socketChannel.connect(new InetSocketAddress("localhost", 8080));
            System.out.println("waiting...");
    }
}
