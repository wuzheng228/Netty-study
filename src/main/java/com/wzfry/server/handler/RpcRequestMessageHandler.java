package com.wzfry.server.handler;

import com.wzfry.message.RpcRequestMessage;
import com.wzfry.message.RpcResponseMessage;
import com.wzfry.server.service.HelloService;
import com.wzfry.server.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        RpcResponseMessage responseMessage = new RpcResponseMessage();
        try {
            // 反射调用 方法 返回 PRC响应

            HelloService service = (HelloService) ServicesFactory.getServices(Class.forName(msg.getInterfaceName()));

            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());

            Object invoke = method.invoke(service, msg.getParameterValue());

            responseMessage.setSequenceId(msg.getSequenceId());
            responseMessage.setReturnValue(invoke);

        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.setExceptionValue(e);
        }
        ctx.writeAndFlush(responseMessage);
    }
}
