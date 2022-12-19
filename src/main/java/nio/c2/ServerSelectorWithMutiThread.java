package nio.c2;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ServerSelectorWithMutiThread {
    public static void main(String[] args) throws Exception {
        BossEventLoop bossEventLoop = new BossEventLoop();
        bossEventLoop.register();
    }

    public static class BossEventLoop implements Runnable {
        private Selector boss;
        private WorkerEventLoop[] workers;
        private volatile boolean start = false;
        private AtomicInteger cnt;

        public void register() throws IOException {
            if (!start) {
                cnt = new AtomicInteger();
                // 初始化server socket channel
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                // channel绑定socket
                boss = Selector.open();
                SelectionKey bossKey = ssc.register(boss, 0, null);
                bossKey.interestOps(SelectionKey.OP_ACCEPT);
                // channel 绑定端口
                ssc.bind(new InetSocketAddress(8080));
                // 初始化worker
                workers = new WorkerEventLoop[2];
                for (int i = 0; i < workers.length; i++) {
                    workers[i] = new WorkerEventLoop(i);
                }
                // 启动boss线程
                Thread bossThread = new Thread(this, "boss");
//                bossThread.setDaemon(true); // 守护线程会在主线程结束后同样结束
                bossThread.start();
                start = true;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.debug(" is daemon {}", Thread.currentThread().isDaemon());
                    boss.select();
                    Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            log.debug("connected {}", sc);
                            workers[cnt.getAndIncrement() % workers.length].register(sc);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class WorkerEventLoop implements Runnable {
        private Selector worker;
        private volatile boolean start = false;
        private int index;

        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();
        public WorkerEventLoop(int index) {
            this.index = index;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                worker = Selector.open();
                new Thread(this, "worker-" + index).start();
                start =true;
            }
            // 执行这个方法的线程是 boss 线程 在boss线程中操作 sc.resigster 绑定 woker的selector会有问题，
            // 这里将绑定woker selector的操作利用队列放入到worker线程中进行
            queue.add(()-> {
                try {
                    log.debug("sc.register select");
                    sc.register(worker, SelectionKey.OP_READ, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            worker.wakeup();// worker线程启动后就会在select()方法处阻塞, 为了将channel绑定到worker的selecter 需要 调用wakeup方法
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.debug("worker.select()");
                    worker.select();
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = worker.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.info("channel {}", channel.getRemoteAddress());
                            int read = channel.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
