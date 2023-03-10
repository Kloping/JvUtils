package io.github.kloping.common;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author github-kloping
 */
public class Public {
    public static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(8, 8,
            10L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>());

    public static ExecutorService EXECUTOR_SERVICE1 = new ThreadPoolExecutor(1, 1,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());


}
