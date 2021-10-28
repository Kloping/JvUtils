package cn.kloping.map;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static cn.kloping.judge.Judge.isNotNull;

public class MapUtils {
    /**
     * 向Map的K List 添加元素 避免 List 为空的情况
     * @param map
     * @param k map k
     * @param v list v
     * @param <K>
     * @param <V>
     */
    public static <K, V> void append(Map<K, List<V>> map, K k, V v) {
        if (!isNotNull(map, k, v)) return;
        List<V> list = map.get(k);
        if (list == null) list = new CopyOnWriteArrayList<>();
        list.add(v);
        map.put(k, list);
    }

    /**
     * 向Map的K List 添加元素 避免 List 为空的情况 且自定义 List 类型
     * @param map
     * @param k map k
     * @param v list v
     * @param cla list type
     * @param <K>
     * @param <V>
     * @param <T>
     */
    public static <K, V, T extends List> void append(Map<K, List<V>> map, K k, V v, Class<T> cla) {
        try {
            if (!isNotNull(map, k, v)) return;
            List<V> list = map.get(k);
            if (list == null) list = cla.newInstance();
            list.add(v);
            map.put(k, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向Map的K Map 添加键值 避免 Map 为null的情况
     * @param map
     * @param mk map k
     * @param k map V map K
     * @param v map V map v
     * @param <mK>
     * @param <K>
     * @param <V>
     */
    public static <mK, K, V> void append(Map<mK, Map<K, V>> map, mK mk, K k, V v) {
        if (!isNotNull(map, mk, k, v)) return;
        Map<K, V> m = map.get(mk);
        if (m == null) m = new ConcurrentHashMap<>();
        m.put(k, v);
        map.put(mk, m);
    }

    /**
     * 向Map的K Map 添加键值 避免 Map 为null的情况 且自定义 map 类型
     * @param map
     * @param mk map k
     * @param k map V map k
     * @param v map V map v
     * @param cla
     * @param <mK>
     * @param <K>
     * @param <V>
     * @param <T>
     */
    public static <mK, K, V, T extends Map> void append(Map<mK, Map<K, V>> map, mK mk, K k, V v, Class<T> cla) {
        try {
            if (!isNotNull(map, mk, k, v)) return;
            Map<K, V> m = map.get(mk);
            if (m == null) m = cla.newInstance();
            m.put(k, v);
            map.put(mk, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
