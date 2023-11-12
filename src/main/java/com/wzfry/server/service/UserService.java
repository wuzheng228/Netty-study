package com.wzfry.server.service;

public interface UserService {
    /**
     * 登陆
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功 true 登陆失败false
     */
    boolean login(String username, String password);
}
