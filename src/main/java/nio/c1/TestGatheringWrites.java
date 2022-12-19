package nio.c1;

import util.ByteBufferUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestGatheringWrites {
    public static void main(String[] args) {
        String file = TestScatteringReads.class.getClassLoader().getResource("3parts.txt").getFile();
        System.out.println(file);
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            FileChannel channel = accessFile.getChannel();
            ByteBuffer buf1 = ByteBuffer.allocate(4);
            ByteBuffer buf2 = ByteBuffer.allocate(4);

            buf1.put(buf2);
            buf1.put(new byte[] {'a', 'b', 'c', 'd'});
            buf2.put(new byte[] {'e', 'f', 'g', 'h'});

            buf1.flip();
//            buf2.flip();
            ByteBufferUtil.debugRead(buf1);

            channel.write(new ByteBuffer[] {buf1, buf2});


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
