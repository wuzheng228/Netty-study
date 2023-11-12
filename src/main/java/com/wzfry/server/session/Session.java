package com.wzfry.server.session;

import io.netty.channel.Channel;

public interface Session {
    /**
     * 绑定会话
     * @param channel 需要绑定会话的channel
     * @param username 需要绑定会话的用户
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     * @param channel 需要解绑会话的channel
     */
    void unbind(Channel channel);

    /**
     * 获取属性
     * @param channel
     * @param name
     * @return
     */
    Object getAttribute(Channel channel, String name);

    /**
     * 设置属性
     * @param channel
     * @param name
     * @param value
     */
    void setAttribute(Channel channel, String name, Object value);

    /**
     * 根据用户名获取Channel
     * @param username
     * @return
     */
    Channel getChannel(String username);

}
