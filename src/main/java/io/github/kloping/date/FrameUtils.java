package io.github.kloping.date;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author github-kloping
 * @version 1.0
 */
public class FrameUtils implements Runnable {
    private boolean ignoreErr = true;
    private int eve = 66;
    private Set<Runnable> frames = new HashSet<>();
    private static int index = 0;
    public static final ScheduledExecutorService SERVICE = new ScheduledThreadPoolExecutor(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "frame-thread-" + getIndex());
        }
    });

    public static final FrameUtils INSTANCE = new FrameUtils();

    public static synchronized int getIndex() {
        return index++;
    }

    @Override
    public void run() {
        for (Runnable frame : frames) {
            if (ignoreErr) {
                try {
                    frame.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                frame.run();
            }
        }
    }

    public FrameUtils() {
        SERVICE.scheduleAtFixedRate(this, eve, eve, TimeUnit.MILLISECONDS);
    }

    public boolean isIgnoreErr() {
        return ignoreErr;
    }

    public void setIgnoreErr(boolean ignoreErr) {
        this.ignoreErr = ignoreErr;
    }

    public int getEve() {
        return eve;
    }

    public void setEve(int eve) {
        this.eve = eve;
    }

    public Set<Runnable> getFrames() {
        return frames;
    }

    public void setFrames(Set<Runnable> frames) {
        this.frames = frames;
    }

    public static void setIndex(int index) {
        FrameUtils.index = index;
    }

    public static void add(Runnable runnable) {
        INSTANCE.getFrames().add(runnable);
    }
}
