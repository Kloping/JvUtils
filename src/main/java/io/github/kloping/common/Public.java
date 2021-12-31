package io.github.kloping.common;

import java.util.Map;
import java.util.concurrent.*;

public class Public {
    public static final ExecutorService THREADS = new ThreadPoolExecutor(15, 15, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(15), new ThreadFactory() {
        private int i = 0;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread("t" + i++);
        }
    });
}
