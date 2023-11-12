package com.wzfry.server.handler;

import com.wzfry.message.LoginRequestMessage;
import com.wzfry.message.LoginResponseMessage;
import com.wzfry.server.service.UserServiceFactory;
import com.wzfry.server.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        LoginResponseMessage resp;
        boolean login = UserServiceFactory.getUserService().login(username, password);
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            resp = new LoginResponseMessage(true, "登陆成功");
        } else {
            resp = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(resp);
    }
}
