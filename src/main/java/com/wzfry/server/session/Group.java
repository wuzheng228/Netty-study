package com.wzfry.server.session;

import java.util.Collections;
import java.util.Set;

public class Group {
    private String name;
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }

    public boolean add(String member) {
        return members.add(member);
    }

    public boolean remove(String member) {
        return members.remove(member);
    }

    public Set<String> getMembers() {
        return members;
    }
}
