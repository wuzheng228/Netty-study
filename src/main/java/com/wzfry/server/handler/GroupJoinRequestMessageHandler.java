package com.wzfry.server.handler;

import com.wzfry.message.GroupJoinRequestMessage;
import com.wzfry.message.GroupJoinResponseMessage;
import com.wzfry.server.session.Group;
import com.wzfry.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        Group group = GroupSessionFactory.getGroupSession().joinMember(username, groupName);
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, groupName + " 加入群聊成功！"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, groupName + "群聊不存在"));
        }
    }
}
