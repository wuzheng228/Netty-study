package com.wzfry.server.handler;

import com.wzfry.message.GroupCreateRequestMessage;
import com.wzfry.message.GroupCreateResponseMessage;
import com.wzfry.server.session.Group;
import com.wzfry.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        Group group = GroupSessionFactory.getGroupSession().createGroup(groupName, members);
        if (group == null) {
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, "群聊：" + groupName + " 创建成功"));
            return;
        }
        ctx.writeAndFlush(new GroupCreateResponseMessage(false, "群聊已存在"));
    }
}
