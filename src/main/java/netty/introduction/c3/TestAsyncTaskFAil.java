package netty.introduction.c3;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAsyncTaskFAil {
    public static void main(String[] args) {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        promise.addListener(future -> {
            log.debug("result: {}", future.isSuccess() ? future.getNow() : future.cause());
        });

        eventExecutors.execute(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RuntimeException runtimeException = new RuntimeException("error...");
            promise.setFailure(runtimeException);
        });

    }
}
