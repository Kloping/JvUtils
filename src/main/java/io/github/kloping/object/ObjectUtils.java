package io.github.kloping.object;

import io.github.kloping.judge.Judge;

import java.util.HashSet;

/**
 * @author github-kloping
 */
public class ObjectUtils {

    /**
     * 所有基本类型转化为 包装类型
     * byte short int long float double boolean char
     *
     * @param object 转换前
     * @return 转换后的
     */
    public static Object baseToPack(Object object) {
        if (object.getClass() == byte.class) {
            object = Byte.valueOf(String.valueOf(object));
        } else if (object.getClass() == short.class) {
            object = Short.valueOf(String.valueOf(object));
        } else if (object.getClass() == int.class) {
            object = Integer.valueOf(String.valueOf(object));
        } else if (object.getClass() == long.class) {
            object = Long.valueOf(String.valueOf(object));
        } else if (object.getClass() == boolean.class) {
            object = Boolean.valueOf(String.valueOf(object));
        } else if (object.getClass() == float.class) {
            object = Float.valueOf(String.valueOf(object));
        } else if (object.getClass() == double.class) {
            object = Double.valueOf(String.valueOf(object));
        } else if (object.getClass() == char.class) {
            object = Character.valueOf((char) object);
        }
        return object;
    }

    /**
     * 所有基本类型转化为 包装类型
     * byte short int long float double boolean char
     *
     * @param objects
     * @return
     */
    public static Object[] baseToPack(Object... objects) {
        Object[] objects1 = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            objects1[i] = baseToPack(objects[i]);
        }
        return objects1;
    }

    /**
     * 基本类型class转包装类型class
     *
     * @param cla 基本
     * @return 包装
     */
    public static Class<?> baseToPack(Class<?> cla) {
        if (cla == byte.class) return Byte.class;
        if (cla == short.class) return Short.class;
        if (cla == int.class) return Integer.class;
        if (cla == long.class) return Long.class;
        if (cla == boolean.class) return Boolean.class;
        if (cla == char.class) return Character.class;
        if (cla == float.class) return Float.class;
        if (cla == double.class) return Double.class;
        return cla;
    }

    private static final HashSet<Class> HASH_SET = new HashSet();

    static {
        HASH_SET.add(byte.class);
        HASH_SET.add(char.class);
        HASH_SET.add(short.class);
        HASH_SET.add(int.class);
        HASH_SET.add(long.class);
        HASH_SET.add(boolean.class);
        HASH_SET.add(float.class);
        HASH_SET.add(double.class);
        for (Class aClass : HASH_SET.toArray(new Class[0])) {
            HASH_SET.add(baseToPack(aClass));
        }
    }

    public static boolean isBaseOrPack(Class<?> cla) {
        return HASH_SET.contains(cla);
    }

    /**
     * 基本类型class转包装class
     *
     * @param clas
     * @return
     */
    public static Class<?>[] baseToPack(Class<?>... clas) {
        for (int i = 0; i < clas.length; i++) {
            clas[i] = baseToPack(clas[i]);
        }
        return clas;
    }


    /**
     * 判断是否 为 子类型
     * 包含相等
     *
     * @param son    class
     * @param father class
     * @return boolean
     */
    public static boolean isSuper(Class<?> son, Class<?> father) {
        if (!Judge.isNotNull(son, father)) return false;
        if (son == father) return true;
        Class<?> sn = son;
        while ((sn = sn.getSuperclass()) != null) {
            if (father == sn) return true;
        }
        return false;
    }

    /**
     * 判断是否 是 Interface
     * 包含相等
     *
     * @param impl       实现类
     * @param _interface 接口类
     * @return boolean
     */
    public static boolean isInterface(Class<?> impl, Class<?> _interface) {
        if (!Judge.isNotNull(impl, _interface)) return false;
        if (impl == _interface) return true;

        Class<?>[] classes = impl.getInterfaces();
        for (Class cls : classes) {
            int n = arrayIndex(classes, _interface);
            if (n > 0) return true;
            else if (isInterface(cls, _interface)) return true;
        }
        return false;
    }

    /**
     * 继承 或 实现
     *
     * @param son
     * @param father
     * @return
     */
    public static boolean isSuperOrInterface(Class<?> son, Class<?> father) {
        return isSuper(son, father) || isInterface(son, father);
    }

    /**
     * index array 元素
     *
     * @param o       元素
     * @param objects 元素组
     * @return 第几个
     */
    public static int arrayIndex(Object o, Object... objects) {
        if (!Judge.isNotNull(o, objects)) return -1;
        int n = 0;
        for (Object _o : objects) {
            if (o == _o || o.equals(_o))
                return n;
            n++;
        }
        return -1;
    }

    private static final String TURE = "TRUE";
    private static final String FALSE = "FALSE";

    /**
     * 字符串 可能转的类型
     *
     * @param m
     * @return
     */
    public static Object maybeType(String m) {
        if (TURE.equals(m.trim().toUpperCase()) || FALSE.equals(m.trim().toUpperCase())) {
            return Boolean.parseBoolean(m.trim());
        }
        try {
            int i = Integer.parseInt(m.trim());
            return i;
        } catch (NumberFormatException e) {
            try {
                long l = Long.parseLong(m.trim());
                return l;
            } catch (NumberFormatException ex) {
                try {
                    float f = Float.parseFloat(m.trim());
                    return f;
                } catch (NumberFormatException exc) {
                    try {
                        double d = Double.parseDouble(m.trim());
                        return d;
                    } catch (NumberFormatException exx) {
                        return m;
                    }
                }
            }
        }
    }
}
