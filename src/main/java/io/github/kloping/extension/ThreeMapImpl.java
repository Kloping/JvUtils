package io.github.kloping.extension;

import java.util.*;

/**
 * @author github.kloping
 */
public class ThreeMapImpl<K, V1, V2> implements ThreeMap<K, V1, V2> {
    private Map<K, V1> map1 = new HashMap<>();
    private Map<K, V2> map2 = new HashMap<>();

    @Override
    public void foreach(BiConsumer2<K, V1, V2> foreach) {
        Set<K> ks = new HashSet<>();
        ks.addAll(map1.keySet());
        ks.addAll(map2.keySet());
        for (K k : ks) {
            foreach.accept(k, map1.get(k), map2.get(k));
        }
    }

    @Override
    public V1 get1(K k) {
        return map1.get(k);
    }

    @Override
    public V2 get2(K k) {
        return map2.get(k);
    }

    @Override
    public V1 put1(K k, V1 v1) {
        return map1.put(k, v1);
    }

    @Override
    public V2 put2(K k, V2 v2) {
        return map2.put(k, v2);
    }

    @Override
    public boolean containKey(K k) {
        return map1.containsKey(k) && map2.containsKey(k);
    }

    @Override
    public Map.Entry<V1, V2> remove(K k) {
        V1 v1 = map1.get(k);
        V2 v2 = map2.get(k);
        map1.remove(k);
        map2.remove(k);
        return new AbstractMap.SimpleEntry<>(v1, v2);
    }

    @Override
    public int size() {
        Set<K> ks = new HashSet<>();
        ks.addAll(map1.keySet());
        ks.addAll(map2.keySet());
        return ks.size();
    }

    @Override
    public void destroy() {
        map1.clear();
        map2.clear();
        map1 = null;
        map2 = null;
    }

    @Override
    public void clear() {
        map1.clear();
        map2.clear();
    }

    @Override
    public Map.Entry<V1, V2> put(K k, V1 v1, V2 v2) {
        V1 v01 = map1.get(k);
        V2 v02 = map2.get(k);
        put1(k, v1);
        put2(k, v2);
        return new AbstractMap.SimpleEntry<>(v01, v02);
    }

    @Override
    public Map.Entry<V1, V2> get(K k) {
        V1 v1 = get1(k);
        V2 v2 = get2(k);
        return new AbstractMap.SimpleEntry<>(v1, v2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreeMapImpl<?, ?, ?> threeMap = (ThreeMapImpl<?, ?, ?>) o;
        return Objects.equals(map1, threeMap.map1) && Objects.equals(map2, threeMap.map2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map1, map2);
    }

    @Override
    public String toString() {
        return "ThreeMapImpl{" +
                "map1=" + map1 +
                ", map2=" + map2 +
                '}';
    }
}
