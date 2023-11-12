package netty.introduction.c5;

import com.sun.xml.internal.stream.util.BufferAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class TestBufSlice {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1,2,3,4});
        origin.readByte();
        System.out.println(ByteBufUtil.prettyHexDump(origin));
        ByteBuf slice = origin.slice();// 无参指定的是 readIndex - writeIndex 之间的部分进行切片 max capacity被固定为这个大小，无法追加write
        System.out.println(ByteBufUtil.prettyHexDump(slice));
        origin.readByte(); // 原始buf 再读一个字节 slice 不受影响，因为slice有自己独立的指针
        System.out.println(ByteBufUtil.prettyHexDump(origin));
        System.out.println(ByteBufUtil.prettyHexDump(slice));
        slice.setByte(2, 9);
        System.out.println(ByteBufUtil.prettyHexDump(origin));
    }
}
