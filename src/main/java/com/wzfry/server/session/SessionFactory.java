package com.wzfry.server.session;

public abstract class SessionFactory {
    private final static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
