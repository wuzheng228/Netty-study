package com.wzfry.server.handler;

import com.wzfry.message.GroupQuitRequestMessage;
import com.wzfry.message.GroupQuitResponseMessage;
import com.wzfry.server.session.Group;
import com.wzfry.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        Group group = GroupSessionFactory.getGroupSession().removeMember(groupName, username);
        if (group != null) {
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "已退出群聊"));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, "群了不存在"));
        }
    }
}
