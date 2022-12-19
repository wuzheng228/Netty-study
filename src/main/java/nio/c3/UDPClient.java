package nio.c3;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@Slf4j
public class UDPClient {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();

        ByteBuffer buffer = StandardCharsets.UTF_8.encode("你好");
        int send = channel.send(buffer, new InetSocketAddress("localhost",8081));

        log.debug("debug: {}", send);
    }
}
