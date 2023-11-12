package netty.introduction.c1;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.EventExecutor;

public class TestEventGroupIterate {
    public static void main(String[] args) {
        DefaultEventLoopGroup eventExecutors = new DefaultEventLoopGroup(2);
        System.out.println(eventExecutors.next());
        System.out.println(eventExecutors.next());
        System.out.println(eventExecutors.next());
        iterateEventGroup(eventExecutors);
    }

    private static void iterateEventGroup(DefaultEventLoopGroup group) {
        for (EventExecutor eventLoop : group) {
            System.out.println(eventLoop);
        }
    }
}
