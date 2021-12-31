package io.github.kloping.arr;

import java.util.List;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Iterator0<T> {
    private T[] ts;
    private int point;

    private Iterator0() {
    }

    public static <T> Iterator0<T> asIterator(List<T> list) {
        Iterator0<T> iterator = new Iterator0<>();
        iterator.ts = (T[]) list.toArray();
        iterator.point = 0;
        return iterator;
    }

    public boolean hasNext() {
        return ts.length > point;
    }

    public T next() {
        return ts[point++];
    }

    public int back() {
        return --point;
    }
}
