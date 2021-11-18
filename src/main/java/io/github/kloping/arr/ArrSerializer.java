package io.github.kloping.arr;

import io.github.kloping.object.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将集合 或数组 按照指定 Rule 转为 String
 */
public class ArrSerializer {
    /**
     * 转换规则
     *
     * @param <T>
     */
    public abstract static class Rule<T> {
        private Class<T> cls;

        public abstract String serializer(T o);

        public Rule(Class<T> cls) {
            this.cls = cls;
        }
    }

    private int mode = 0;

    public ArrSerializer() {
    }

    /**
     * 添加一个转换规则
     *
     * @param rule
     * @param <T>
     * @return
     */
    public <T> Rule<T> add(Rule<T> rule) {
        try {
            if (maps.containsKey(rule.cls)) return maps.get(rule.cls);
            else return null;
        } finally {
            maps.put(rule.cls, rule);
        }
    }

    private Map<Class<?>, Rule> maps = new ConcurrentHashMap<>();

    /**
     * 开始转换
     *
     * @param collection
     * @return
     */
    public String serializer(Collection collection) {
        StringBuilder sb = new StringBuilder();
        collection.forEach(o -> sb.append(worker(o)));
        return sb.toString();
    }

    /**
     * 开始转换
     *
     * @param objects
     * @return
     */
    public String serializer(Object[] objects) {
        return serializer(Arrays.asList(objects));
    }

    private String worker(Object o) {
        switch (mode) {
            case 0:
                return worker0(o);
            case 1:
                return worker1(o);
            default:
                return "";
        }
    }

    private String worker0(Object o) {
        return maps.containsKey(o.getClass()) ?
                maps.get(o.getClass()).serializer(o) :
                "";
    }

    private Map<Class<?>, Class<?>> memorySonCls2FatherClsMaps = new ConcurrentHashMap<>();

    private String worker1(Object o) {
        if (memorySonCls2FatherClsMaps.containsKey(o.getClass()))
            return maps.get(memorySonCls2FatherClsMaps.get(o.getClass())).serializer(o);
        for (Class<?> cla : maps.keySet()) {
            if (ObjectUtils.isSuperOrInterface(o.getClass(), cla)) {
                memorySonCls2FatherClsMaps.put(o.getClass(), cla);
                return maps.get(cla).serializer(o);
            }
        }
        return "";
    }

    /**
     * 设置模式
     * 默认 0
     * 0 class类型 相等
     * 1 instanceof 模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    private boolean memoryK = false;

    /**
     * 开启记忆
     * 减少 相同类型的 匹配
     */
    public void openMemory() {
        memoryK = true;
    }
}
