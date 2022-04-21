package io.github.kloping.extension;

import java.util.Map.Entry;

/**
 * @author github.kloping
 */
public interface ThreeMap<K, V1, V2> extends Destroyable, Clearable {
    /**
     * get value1
     *
     * @param k
     * @return
     */
    public V1 get1(K k);

    /**
     * get value2
     *
     * @param k
     * @return
     */
    public V2 get2(K k);

    /**
     * put value1
     *
     * @param k
     * @param v1
     * @return
     */
    public V1 put1(K k, V1 v1);

    /**
     * put value2
     *
     * @param k
     * @param v2
     * @return
     */
    public V2 put2(K k, V2 v2);

    /**
     * contain key
     *
     * @param k
     * @return
     */
    public boolean containKey(K k);

    /**
     * for each 遍历
     *
     * @param foreach
     */
    public void foreach(BiConsumer2<K, V1, V2> foreach);

    /**
     * remove value
     *
     * @param k
     * @return
     */
    public Entry<V1, V2> remove(K k);

    /**
     * size
     *
     * @return
     */
    public int size();
}
