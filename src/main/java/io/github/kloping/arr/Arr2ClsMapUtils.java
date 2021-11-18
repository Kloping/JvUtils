package io.github.kloping.arr;

import io.github.kloping.map.MapUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Arr2ClsMapUtils {
    /**
     * 将一组 集合 转为 以其 Class 为 key List为 value 的 Map
     *
     * @param collection
     * @return
     */
    public static Map<Class<?>, List<Object>> arr2map(Collection collection) {
        Map<Class<?>, List<Object>> maps = new LinkedHashMap<>();
        collection.forEach(e -> {
            MapUtils.append(maps, e.getClass(), e);
        });
        return maps;
    }

    /**
     * 将一组 集合 转为 以其 Class 为 key List为 value 的 Map
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T extends Object> Map<Class<?>, List<T>> arr2mapWithType(Collection<T> collection) {
        Map<Class<?>, List<T>> maps = new LinkedHashMap<>();
        collection.forEach(e -> {
            MapUtils.append(maps, e.getClass(), e);
        });
        return maps;
    }
}
