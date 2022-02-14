package io.github.kloping.serialize;

import io.github.kloping.arr.Iterator0;
import io.github.kloping.clasz.ClassUtils;
import io.github.kloping.object.ObjectUtils;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 * @version 1.0
 */
public class HMLObject {
    private Map<String, Object> entry = new ConcurrentHashMap<>();
    private static final String NULL = "null";
    private static final String PRE = "->";
    private static final String TYPE_FLAG = "--type";
    private static final String IGNORE_PRE = "//";
    public static Map<Class<?>, GetFields> customField = new ConcurrentHashMap<>();

    public static interface GetFields<T> {
        /**
         * return fields
         *
         * @param cla
         * @return
         */
        Field[] getFields(Class<T> cla);
    }

    static {
        try {
            Field field = File.class.getDeclaredField("path");
            field.setAccessible(true);
            Field[] fields = new Field[]{field};
            customField.put(File.class, cla -> fields);
            Field kName = Entry0.class.getDeclaredField("kName");
            Field vName = Entry0.class.getDeclaredField("vName");
            Field k = Entry0.class.getDeclaredField("k");
            Field v = Entry0.class.getDeclaredField("v");
            Field[] entry0Fs = new Field[]{kName, vName, k, v};
            customField.put(Entry0.class, cla -> entry0Fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toHMLString(Object o) {
        int t = 0;
        if (o == null) {
            return NULL;
        }
        Class cla = ObjectUtils.baseToPack(o.getClass());
        if (o instanceof Map) {
            return toV3(o, t);
        } else if (o instanceof Collection) {
            return toV4(o, t);
        } else if (o.getClass().isArray()) {
            return toV2(o, t);
        } else if (Number.class.isAssignableFrom(cla)) {
            return PRE + cla.getName() + "\nvalue: " + o.toString();
        } else if (Boolean.class == cla) {
            return PRE + cla.getName() + "\nvalue: " + o.toString();
        } else {
            if (cla == String.class) {
                return PRE + cla.getName() + "\nvalue: " + "\"" + o.toString() + "\"";
            } else {
                return work(o, t + 1);
            }
        }
    }

    private static String work(Object o, int t) {
        Field[] fields = getFields(o.getClass());
        StringWriter sw = new StringWriter();
        sw.write(PRE);
        sw.write(o.getClass().getName());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sw.write("\n");
                for (int i = 0; i < t; i++) {
                    sw.write("\t");
                }
                sw.write(field.getName());
                sw.write(": ");
                sw.write(toV(field.get(o), t));
            } catch (Exception e) {
                System.err.println("warring: " + e);
            }
        }
        return sw.toString().trim();
    }

    private static String work2(Object[] os, int t) {
        StringWriter sw = new StringWriter();
        sw.write(PRE);
        sw.write(os.getClass().getName());
        return work2(os, t, sw);
    }

    private static String work2(Object[] os, int t, String name) {
        StringWriter sw = new StringWriter();
        sw.write(PRE);
        sw.write(name);
        return work2(os, t, sw);
    }

    private static String work2(Object[] os, int t, StringWriter sw) {
        int n = 0;
        for (Object o : os) {
            try {
                sw.write("\n");
                for (int i = 0; i < t; i++) {
                    sw.write("\t");
                }
                sw.write(String.valueOf(n++));
                sw.write(": ");
                sw.write(toV(o, t));
            } catch (Exception e) {
                System.err.println("warring: " + e);
            }
        }
        return sw.toString().trim();
    }

    private static String toV(Object o, int t) {
        if (o == null) {
            return NULL;
        }
        Class cla = ObjectUtils.baseToPack(o.getClass());
        if (o instanceof Map) {
            return toV3(o, t);
        } else if (o instanceof Collection) {
            return toV4(o, t);
        } else if (o.getClass().isArray()) {
            return toV2(o, t);
        } else if (Number.class.isAssignableFrom(cla)) {
            return o.toString();
        } else if (Boolean.class == cla) {
            return o.toString();
        } else {
            if (cla == String.class) {
                return "\"" + o.toString() + "\"";
            } else {
                return work(o, t + 1);
            }
        }
    }

    private static String toV2(Object o, int t) {
        Object[] os = (Object[]) o;
        return work2(os, t + 1);
    }

    private static String toV3(Object o, int t) {
        Map map = (Map) o;
        return work2(Entry0.asEntry0(map), t + 1, o.getClass().getName());
    }

    private static String toV4(Object o, int t) {
        Collection collection = (Collection) o;
        Object[] os = ((Collection<?>) o).toArray();
        return work2(os, t + 1, o.getClass().getName());
    }

    public static HMLObject parseObject(String hmlStr) {
        Iterator0<String> iterator = Iterator0.asIterator(Arrays.asList(hmlStr.split("\n")));
        return parseObject(iterator, 1);
    }

    public static HMLObject parseObject(String hmlStr, int t) {
        Iterator0<String> iterator = Iterator0.asIterator(Arrays.asList(hmlStr.split("\n")));
        return parseObject(iterator, t);
    }

    public static <T> T parseObject(String hmlStr, Class<T> cla) throws ClassNotFoundException {
        return (T) parseObject(hmlStr).toJavaObject();
    }

    public static HMLObject parseObject(Iterator0<String> iterator, int t) {
        HMLObject object = new HMLObject();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.trim().startsWith(IGNORE_PRE)) {
                continue;
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            if (line.trim().startsWith(PRE)) {
                String type = line.trim().substring(PRE.length());
                object.entry.put(TYPE_FLAG, type);
            } else {
                String k = line.substring(0, line.indexOf(":")).trim();
                String v = line.substring(line.indexOf(":") + 1);
                Object vo = toK(v, iterator, 1);
                object.entry.put(k, vo);
            }
        }
        return object;
    }

    private static Object toK(String v, Iterator0<String> iterator, int i) {
        if (NULL.equals(v)) {
            return null;
        } else {
            if (!v.trim().startsWith(PRE)) {
                Object o = ObjectUtils.maybeType(v);
                if (o != v) {
                    return o;
                } else {
                    v = v.trim();
                    v = v.substring(1, v.length() - 1);
                    return v;
                }
            } else {
                String pre = "";
                for (int i1 = 0; i1 < i; i1++) {
                    pre += "\t";
                }
                StringWriter sw = new StringWriter();
                sw.write(v);
                sw.write("\n");
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    if (line.trim().startsWith(IGNORE_PRE)) {
                        continue;
                    }
                    if (preMore(pre, line)) {
                        sw.write(line.replaceFirst(pre, ""));
                        sw.write("\n");
                    } else {
                        iterator.back();
                        break;
                    }
                }
                return parseObject(sw.toString(), i + 1);
            }
        }
    }

    private static boolean preMore(String pre, String line) {
        line = line.replaceFirst(pre, "");
        return line.startsWith(pre);
    }

    private static final Class ENTRY0ARRAY_CLASS = Entry0[].class;

    public static Map<Class<?>, Field[]> cache = new ConcurrentHashMap<>();

    private static Field[] getFields(Class<?> cla) {
        if (cache.containsKey(cla)) {
            return cache.get(cla);
        }
        try {
            if (customField.containsKey(cla)) {
                GetFields getFieldsable = customField.get(cla);
                Field[] fields = getFieldsable.getFields(cla);
                cache.put(cla, fields);
                return fields;
            }
        } catch (Exception e) {
            System.err.println("warring get field " + e);
        }
        Field[] declaredFields = cla.getDeclaredFields();
        Set<Field> fields1 = new HashSet<>();
        for (Field field : declaredFields) {
            if (ClassUtils.isStatic(field)) {
                continue;
            }
            fields1.add(field);
        }
        Field[] fields = fields1.toArray(new Field[0]);
        cache.put(cla, fields);
        return fields;
    }

    @Override
    public String toString() {
        return "HMLObject{" +
                "entry=" + entry +
                '}';
    }

    public Object toJavaObject() throws ClassNotFoundException {
        String className = entry.get(TYPE_FLAG).toString();
        Class cla = Class.forName(className);
        if (Map.class.isAssignableFrom(cla)) {
            return toJavaObject3(cla);
        } else if (Collection.class.isAssignableFrom(cla)) {
            return toJavaObject5(cla);
        } else if (cla.isArray()) {
            return toJavaObject2(cla);
        } else {
            return toJavaObject(cla);
        }
    }

    private Object toJavaObject5(Class cla) throws ClassNotFoundException {
        Collection col = (Collection) ClassUtils.newInstance(cla);
        Object[] objects = (Object[]) Array.newInstance(Object.class, getRealSize());
        for (String k : entry.keySet()) {
            if (TYPE_FLAG.equals(k)) {
                continue;
            }
            try {
                int i = Integer.parseInt(k);
                Object v = entry.get(k);
                if (v instanceof HMLObject) {
                    HMLObject o1 = (HMLObject) v;
                    objects[i] = o1.toJavaObject();
                } else {
                    objects[i] = v;
                }
            } catch (Exception e) {
                System.err.println("warring for " + e);
            }
        }
        col.addAll(Arrays.asList(objects));
        return col;
    }

    private Object toJavaObject3(Class cla) throws ClassNotFoundException {
        Map map = (Map) ClassUtils.newInstance(cla);
        Entry0[] entry0s = (Entry0[]) Array.newInstance(Entry0.class, getRealSize());
        for (String k : entry.keySet()) {
            if (TYPE_FLAG.equals(k)) {
                continue;
            }
            int i = Integer.parseInt(k);
            Object v = entry.get(k);
            HMLObject o1 = (HMLObject) v;
            entry0s[i] = (Entry0) o1.toJavaObject4();
        }
        Entry0.asMap(entry0s, map);
        return map;
    }

    private Object toJavaObject4() {
        Class cla = Entry0.class;
        Field[] fields = getFields(cla);
        Entry0 t = ClassUtils.newInstance(Entry0.class);
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fn = field.getName();
                Class cla0 = field.getType();
                Object o = entry.get(fn);
                if ("k".equals(field.getName())) {
                    o = ClassUtils.asT(t.kName, o);
                    cla0 = o.getClass();
                }
                if ("v".equals(field.getName())) {
                    o = ClassUtils.asT(t.vName, o);
                    cla0 = o.getClass();
                }
                if (entry.containsKey(fn)) {
                    if (ObjectUtils.isBaseOrPack(cla0)) {
                        field.set(t, o);
                    } else if (cla0 == String.class) {
                        String oStr = o.toString();
                        field.set(t, oStr);
                    } else {
                        if (o instanceof HMLObject) {
                            HMLObject ho = (HMLObject) o;
                            o = ho.toJavaObject();
                            field.set(t, o);
                        } else {
                            System.err.println("unknown type " + cla0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    private <T> T[] toJavaObject2(Class<T> cla) throws ClassNotFoundException {
        Class c0 = cla.getComponentType();
        T[] objects = (T[]) Array.newInstance(c0, getRealSize());
        for (String k : entry.keySet()) {
            k = k.trim();
            if (TYPE_FLAG.equals(k)) {
                continue;
            }
            Object v = entry.get(k);
            int i = Integer.parseInt(k);
            if (v instanceof HMLObject) {
                HMLObject o1 = (HMLObject) v;
                objects[i] = (T) o1.toJavaObject();
            } else {
                objects[i] = (T) v;
            }
        }
        return objects;
    }

    public <T> T toJavaObject(Class<T> cla) {
        Field[] fields = getFields(cla);
        T t = ClassUtils.newInstance(cla);
        Object[] objects = new Object[fields.length];
        int i = 0;
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Class cla0 = field.getType();
                String fn = field.getName();
                if (entry.containsKey(fn)) {
                    Object o = entry.get(fn);
                    if (ObjectUtils.isBaseOrPack(cla0)) {
                        if (t == null) {
                            objects[i] = o;
                        } else {
                            field.set(t, ObjectUtils.asPossible(cla0, o));
                        }
                    } else if (cla0 == String.class) {
                        String oStr = o.toString();
                        if (t == null) {
                            objects[i] = oStr;
                        } else {
                            field.set(t, oStr);
                        }
                    } else {
                        if (o instanceof HMLObject) {
                            HMLObject ho = (HMLObject) o;
                            o = ho.toJavaObject();
                            if (t == null) {
                                objects[i] = o;
                            } else {
                                field.set(t, o);
                            }
                        } else {
                            System.err.println("unknown type " + cla0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        if (t == null) {
            t = ClassUtils.newInstance(cla, objects);
        }
        return t;
    }

    private synchronized int getRealSize() {
        int i = 0;
        for (String s : entry.keySet()) {
            if (s.equals(TYPE_FLAG)) {
                continue;
            }
            i++;
        }
        return i;
    }

    public Map<String, Object> getEntry() {
        return entry;
    }

    public void setEntry(Map<String, Object> entry) {
        this.entry = entry;
    }

    private static class Entry0<K, V> {

        private K k;
        private V v;
        private String kName;
        private String vName;

        private Entry0() {
        }

        public static <K, V> Entry0<K, V>[] asEntry0(Map<K, V> map) {
            Entry0<K, V>[] entry0s = new Entry0[map.size()];
            int i = 0;
            for (Entry<K, V> kvEntry : map.entrySet()) {
                Entry0<K, V> entry0 = new Entry0<K, V>();
                entry0.k = kvEntry.getKey();
                entry0.v = kvEntry.getValue();
                entry0.kName = kvEntry.getKey().getClass().getName();
                entry0.vName = kvEntry.getValue().getClass().getName();
                entry0s[i++] = entry0;
            }
            return entry0s;
        }

        public static <K, V> Map<K, V> asMap(Entry0<K, V>[] entry0s, Map<K, V> map) throws ClassNotFoundException {
            for (Entry0<K, V> entry0 : entry0s) {
                K k = ClassUtils.asT(entry0.kName, entry0.k);
                V v = ClassUtils.asT(entry0.vName, entry0.v);
                map.put(k, v);
            }
            return map;
        }
    }

    public static Map<Class<?>, GetFields> getCustomField() {
        return customField;
    }

    public static Map<Class<?>, Field[]> getCache() {
        return cache;
    }
}












