package com.wzfry.server.session;

public abstract class GroupSessionFactory {
    private final static GroupSession groupSession = new GroupSessionMemeryImpl();

    public static GroupSession getGroupSession() {
        return groupSession;
    }
}
