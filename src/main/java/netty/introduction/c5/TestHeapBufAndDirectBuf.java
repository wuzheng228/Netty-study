package netty.introduction.c5;

import com.sun.xml.internal.stream.util.BufferAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class TestHeapBufAndDirectBuf {
    public static void main(String[] args) {
        ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer();
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer();
    }
}
