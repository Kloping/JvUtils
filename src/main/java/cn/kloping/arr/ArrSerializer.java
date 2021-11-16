package cn.kloping.arr;

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
     * @param <T>
     */
    public abstract static class Rule<T> {
        private Class<T> cls;

        public abstract String serializer(T o);

        public Rule(Class<T> cls) {
            this.cls = cls;
        }
    }

    public ArrSerializer() {
    }

    /**
     * 添加一个转换规则
     * @param rule
     * @param <T>
     * @return
     */
    public <T> Rule<T> add(Rule<T> rule) {
        try {if (maps.containsKey(rule.cls)) return maps.get(rule.cls);else return null;}
        finally {maps.put(rule.cls, rule);}
    }

    private Map<Class<?>, Rule> maps = new ConcurrentHashMap<>();

    /**
     * 开始转换
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
     * @param objects
     * @return
     */
    public String serializer(Object[] objects) {
        return serializer(Arrays.asList(objects));
    }

    private String worker(Object o) {
        return maps.containsKey(o.getClass()) ? maps.get(o.getClass()).serializer(o) : "";
    }
}
