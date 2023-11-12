package netty.introduction.c5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TestByteBufWrite {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer(10);
        byteBuf.writeBytes(new byte[] {1, 2, 3, 4});
        log(byteBuf);
        byteBuf.writeInt(5);
        log(byteBuf);
        byteBuf.writeInt(6);
        log(byteBuf);
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());
        log(byteBuf);
        byteBuf.markReaderIndex();
        System.out.println(byteBuf.readInt());
        log(byteBuf);
        byteBuf.resetReaderIndex();
        log(byteBuf);
    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
