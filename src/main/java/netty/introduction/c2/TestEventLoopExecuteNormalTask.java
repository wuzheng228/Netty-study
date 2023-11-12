package netty.introduction.c2;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEventLoopExecuteNormalTask {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup nioworkers = new NioEventLoopGroup(2);

        log.debug("Server start...");
        Thread.sleep(2000);

        nioworkers.execute(()->{
            log.debug("normal task...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("main thread ended...");

    }
}
