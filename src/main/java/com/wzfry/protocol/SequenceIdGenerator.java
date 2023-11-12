package com.wzfry.protocol;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequenceIdGenerator {
    private final static AtomicInteger cnt = new AtomicInteger();

    public static Integer next() {
        return cnt.incrementAndGet();
    }
}
