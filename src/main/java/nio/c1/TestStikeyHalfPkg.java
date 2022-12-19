package nio.c1;

import util.ByteBufferUtil;

import java.nio.ByteBuffer;

public class TestStikeyHalfPkg {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(36);
        byteBuffer.put("Hello,world\nI'm zhangsan\nHo".getBytes()); // 黏包

        split(byteBuffer);
        byteBuffer.put("w are you?\nhaha!\n".getBytes());
        split(byteBuffer);
    }

    public static void split(ByteBuffer buffer) {
        buffer.flip();
        ByteBufferUtil.debugAll(buffer);
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
