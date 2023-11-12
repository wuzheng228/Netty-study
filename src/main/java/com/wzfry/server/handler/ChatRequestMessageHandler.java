package com.wzfry.server.handler;

import com.wzfry.message.ChatRequestMessage;
import com.wzfry.message.ChatResponseMessage;
import com.wzfry.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
        else {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线test"));
        }
    }
}
