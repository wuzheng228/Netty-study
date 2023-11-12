package netty.introduction.c3;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSyncExecuteTask {
    public static void main(String[] args) throws Exception {
        DefaultEventLoop eventExecutor = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutor);

        eventExecutor.execute(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("set success, {}", 10);
            promise.setSuccess(10);
        });

        log.debug("start....");
        log.debug("promise getNow {}", promise.getNow()); // promise get
        log.debug("{}", promise.get());
    }
}
