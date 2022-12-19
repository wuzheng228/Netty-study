package nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@Slf4j
public class TestFileChannel {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile(TestFileChannel.class.getClassLoader().getResource("data.txt").getFile(), "rw") ) {
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(5);
            do {
                int len = fileChannel.read(buffer);
                log.info("len: {}", len);
                if (len == -1)
                    break;
                buffer.flip();
                while (buffer.hasRemaining()) {
                    log.info("{}", (char) buffer.get());
                }
                buffer.clear();
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
