package io.github.kloping.arr;

import java.io.*;
import java.util.List;

/**
 * @author github-kloping
 * @version 1.0
 */
public abstract class Iterator0<T> {
    protected T[] ts;
    protected int point;

    private Iterator0() {
    }

    public static <T> Iterator0<T> asIterator(List<T> list) {
        Iterator0<T> iterator = new Iterator0<T>() {
            @Override
            public boolean hasNext() {
                return this.ts.length > this.point;
            }

            @Override
            public T next() {
                return ts[point++];
            }

            @Override
            public int back() {
                return --point;
            }
        };
        iterator.ts = (T[]) list.toArray();
        iterator.point = 0;
        return iterator;
    }

    /**
     * this method don't read all at once
     * but  No matter how many times it's called
     * {@link Iterator0#back()} The first call after {@link Iterator0#next()} takes effect
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Iterator0<String> asIterator(File file) throws IOException {
        Iterator0<String> iterator = new Iterator0<String>() {
            private BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            {
                ts = new String[3];
                ts[0] = br.readLine();
                ts[1] = br.readLine();
                ts[2] = br.readLine();
                point = 0;
            }

            @Override
            public boolean hasNext() {
                return ts[point] != null;
            }

            @Override
            public String next() {
                try {
                    return ts[point];
                } finally {
                    if (point == 1) {
                        ts[0] = ts[1];
                        ts[1] = ts[2];
                        try {
                            ts[2] = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        point++;
                    }
                }
            }

            @Override
            public int back() {
                return point = 0;
            }
        };

        return iterator;
    }

    /**
     * has element
     *
     * @return
     */
    public abstract boolean hasNext();

    /**
     * next element
     *
     * @return
     */
    public abstract T next();

    /**
     * go back
     *
     * @return
     */
    public abstract int back();
}
