package netty.introduction.c3;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSyncTaskFail {
    public static void main(String[] args) {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RuntimeException runtimeException = new RuntimeException("error...");
            promise.setFailure(runtimeException);
        });

        try {
//            promise.sync();
//            promise.get();
            promise.await(); // await 不会抛出异常
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!promise.isSuccess()) {
            log.debug("error: ",promise.cause());
        }

    }
}
