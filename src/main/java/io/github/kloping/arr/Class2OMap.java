package io.github.kloping.arr;

import io.github.kloping.object.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class Class2OMap {
    private Map<Class<?>, List<Object>> map = new LinkedHashMap<>();
    private Map<Class<?>, Class<?>> memorySonCls2FatherClsMaps = new ConcurrentHashMap<>();

    /**
     * 创建一个  Class2OMap
     *
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> Class2OMap create(T... objects) {
        return new Class2OMap(objects);
    }

    private <M> Class2OMap(M... objects) {
        List<M> list = new LinkedList<>();
        for (M o : objects) {
            if (o == null) continue;
            if (o instanceof Collection) {
                Collection collection = (Collection) o;
                list.addAll(collection);
            } else if (o.getClass().isArray()) {
                Object[] ojbs = (Object[]) o;
                Collection collection = Arrays.asList(objects);
                list.addAll(collection);
            } else list.add(o);
        }
        map = Arr2ClsMapUtils.arr2map(list);
    }

    public boolean isIdentical() {
        return identical;
    }

    /**
     * 设置 class 类型 必须 是否 相等 (不包含子类)
     *
     * @param identical
     */
    public void setIdentical(boolean identical) {
        this.identical = identical;
    }

    public boolean isMemory() {
        return memory;
    }

    /**
     * 设置是否启用 记忆 减少下次匹配 class 的 时间
     * tips: 若关闭Identical 且 若 项目总类较多 建议开启
     *
     * @param memory
     */
    public void setMemory(boolean memory) {
        this.memory = memory;
    }

    private boolean identical = true;
    private boolean memory = false;


    /**
     * 根据类型 获取 一个 实例
     *
     * @param cla
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> cla) {
        return (T) (identical ? get0(cla) : get1(cla));
    }

    /**
     * 根据类型 获取 第 index 个 实例
     *
     * @param cla
     * @param index
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> cla, int index) {
        return identical ? get0(cla, index) : get1(cla, index);
    }

    /**
     * 根据类型 获取 其 大小
     *
     * @param cla
     * @return
     */
    public Integer getSize(Class<?> cla) {
        return identical ? getSize0(cla) : getSize1(cla);
    }

    /**
     * 根据类型 获取 其 所在的 List
     *
     * @param cla
     * @param <T>
     * @param <T1>
     * @return
     */
    public <T, T1> List<T> getList(Class<T1> cla) {
        return identical ? getList0(cla) : getList1(cla);
    }

    private <T> T get0(Class<T> cla) {
        if (map.containsKey(cla))
            return (T) map.get(cla).get(0);
        else return null;
    }

    private Integer getSize0(Class<?> cla) {
        if (map.containsKey(cla))
            return map.get(cla).size();
        else return null;
    }

    private List getList0(Class<?> cla) {
        if (map.containsKey(cla))
            return map.get(cla);
        else return null;
    }

    private <T> T get0(Class<T> cla, int index) {
        if (map.containsKey(cla))
            if (map.get(cla).size() > index)
                return (T) map.get(cla).get(index);
            else return null;
        else return null;
    }

    private <T> T get1(Class<T> cla) {
        AtomicReference<T> t = new AtomicReference<>();
        if (memorySonCls2FatherClsMaps.containsKey(cla)) return (T) map.get(memorySonCls2FatherClsMaps.get(cla)).get(0);
        map.forEach((k, v) -> {
            if (ObjectUtils.isSuperOrInterface(k, cla)) {
                t.set((T) v.get(0));
                if (memory) memorySonCls2FatherClsMaps.put(k, cla);
                return;
            }
        });
        if (t == null) return null;
        else return t.get();
    }

    private Integer getSize1(Class<?> cla) {
        AtomicInteger size = new AtomicInteger(-1);
        if (memorySonCls2FatherClsMaps.containsKey(cla)) return map.get(memorySonCls2FatherClsMaps.get(cla)).size();
        map.forEach((k, v) -> {
            if (ObjectUtils.isSuperOrInterface(k, cla)) {
                size.set(v.size());
                if (memory) memorySonCls2FatherClsMaps.put(k, cla);
                return;
            }
        });
        return size.get();
    }

    private List getList1(Class<?> cla) {
        AtomicReference<List> list = new AtomicReference<>();
        if (memorySonCls2FatherClsMaps.containsKey(cla)) return map.get(memorySonCls2FatherClsMaps.get(cla));
        map.forEach((k, v) -> {
            if (ObjectUtils.isSuperOrInterface(k, cla)) {
                list.set(v);
                if (memory) memorySonCls2FatherClsMaps.put(k, cla);
                return;
            }
        });
        return list.get();
    }

    private <T> T get1(Class<T> cla, int index) {
        AtomicReference<T> t = new AtomicReference<>();
        if (memorySonCls2FatherClsMaps.containsKey(cla)) {
            List v = map.get(memorySonCls2FatherClsMaps.get(cla));
            if (v.size() > index) t.set((T) v.get(0));
            return (T) map.get(memorySonCls2FatherClsMaps.get(cla)).get(index);
        }
        map.forEach((k, v) -> {
            if (ObjectUtils.isSuperOrInterface(k, cla)) {
                if (memory) memorySonCls2FatherClsMaps.put(k, cla);
                if (v.size() > index) t.set((T) v.get(index));
                else return;
            }
        });
        if (t == null) return null;
        else return t.get();
    }
}
