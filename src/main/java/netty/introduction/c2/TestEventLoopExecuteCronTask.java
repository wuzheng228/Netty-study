package netty.introduction.c2;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoopExecuteCronTask {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        log.debug("server start....");
        eventExecutors.scheduleAtFixedRate(()->{
            log.debug("running");
        }, 0, 1, TimeUnit.SECONDS);
    }
}
