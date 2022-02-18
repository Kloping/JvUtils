package io.github.kloping.common;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author github-kloping
 */
public class Public {
    public static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(8, 8,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
}
