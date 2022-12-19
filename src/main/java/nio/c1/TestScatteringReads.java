package nio.c1;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestScatteringReads {
    public static void main(String[] args) {
        String file = TestScatteringReads.class.getClassLoader().getResource("words.txt").getFile();
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            FileChannel channel = accessFile.getChannel();
            ByteBuffer buf1 = ByteBuffer.allocate(3);
            ByteBuffer buf2 = ByteBuffer.allocate(3);
            ByteBuffer buf3 = ByteBuffer.allocate(6);
            long read = channel.read(new ByteBuffer[]{buf1, buf2, buf3});
            log.info("read bytes {}", read);

            buf1.flip();
            buf2.flip();
            buf3.flip();

            ByteBufferUtil.debugRead(buf1);
            ByteBufferUtil.debugRead(buf2);
            ByteBufferUtil.debugRead(buf3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
