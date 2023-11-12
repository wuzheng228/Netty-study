package netty.introduction.c3;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAwaitDeadLock {
    public static void main(String[] args) {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(()->{
            log.debug("1");
            try {
                promise.await();// 再event loop 线程当中调用阻塞操作会造成死锁 抛出BlockingOperationException
            } catch (Exception e) {
                log.debug("", e);
            }
            log.debug("2");
        });

//        eventExecutors.execute(()->{
//            try {
//                promise.await();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }
}
