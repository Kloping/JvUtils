package io.github.kloping.rand;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author github.kloping
 */
public class RandomUtils {
    public static final Random RANDOM = new SecureRandom();

    /**
     * get random
     * @param ts
     * @return
     * @param <T>
     */
    public static <T> T getRand(T... ts) {
        return ts[RANDOM.nextInt(ts.length)];
    }

    /**
     * get rand integer
     *
     * @param from max
     * @param end  min
     * @return
     */
    public static Integer getRandInteger(int from, int end) {
        return RANDOM.nextInt(from - end) + end;
    }
}
