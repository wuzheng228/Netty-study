package com.wzfry.server.service;

public abstract class UserServiceFactory {
    private static UserService userService = new UserServiceMemeryImpl();
    public static UserService getUserService() {
        return userService;
    }
}
