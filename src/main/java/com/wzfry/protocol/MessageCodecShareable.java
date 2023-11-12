package com.wzfry.protocol;

import com.wzfry.config.Config;
import com.wzfry.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class MessageCodecShareable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 4 字节魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2 .1 字节的版本
        out.writeByte(1);
        // 3. 1字节 序列化方式 jdk 0, json 1
//        out.writeByte(0);
        out.writeByte(Config.getSerilizerAlgorithm().ordinal());
        // 4. 1字节 消息类型
        out.writeByte(msg.getMessageType());
        // 5. 4个字节 sequenceId
        out.writeInt(msg.getSequenceId());
        // 填充字节
        out.writeByte(0xff);

        byte[] bytes = Config.getSerilizerAlgorithm().serilizer(msg);
//        byte[] bytes = Serilizer.Algorithm.Java.serilizer(msg);
        // 长度
        out.writeInt(bytes.length);
        // 内容
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        Serllizer.Algorithm algorithm = Serllizer.Algorithm.values()[serializerType];

        Class<? extends Message> messageClass = Message.getMessageClass(messageType);

        Message message = algorithm.deserilizer(messageClass, bytes);// 因为服务器和客户端在不同的机器上,所以需要使用消息指定的反序列化算法

//        Object message = Serilizer.Algorithm.Java.deserilizer(Object.class, bytes);
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);
    }
}
