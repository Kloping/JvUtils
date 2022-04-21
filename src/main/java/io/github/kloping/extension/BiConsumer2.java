package io.github.kloping.extension;

/**
 * @author github.kloping
 */
public interface BiConsumer2<K, V1, V2> {
    /**
     * accept
     *
     * @param k
     * @param v1
     * @param v2
     */
    void accept(K k, V1 v1, V2 v2);
}
