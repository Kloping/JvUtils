package cn.kloping.clasz;

import cn.kloping.map.MapUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qq-3474006766
 */
public class ClassUtils {
    public static class ClassEntity {
        public Method[] methods;
        public Field[] fields;
        public Map<String, Field> fieldMap;
        public Map<String, Method> methodMap;
        public Map<Class<?>, List<Field>> cls2field;
        public Map<Class<?>, List<Method>> cls2methods;

        private ClassEntity() {
        }
    }

    private static Method defineClassMethod;

    static {
        try {
            defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            defineClassMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个class的管理
     *
     * @param bytes     数据
     * @param className class 名字 例如 com.baidu.Main
     * @return ClassEntity
     * @throws Exception
     */
    public static ClassEntity createClassEntity(byte[] bytes, String className) throws Exception {
        defineClassMethod.setAccessible(true);
        Class<?> cla = (Class<?>) defineClassMethod.invoke(ClassLoader.getSystemClassLoader(), className, bytes, 0, bytes.length);
        defineClassMethod.setAccessible(false);
        ClassEntity entity = new ClassEntity();
        entity.methods = getAllMethod(cla);
        entity.fields = getAllField(cla);
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        Map<String, Method> methodMap = new LinkedHashMap<>();
        Map<Class<?>, List<Field>> cls2field = new LinkedHashMap<>();
        Map<Class<?>, List<Method>> cls2methods = new LinkedHashMap<>();
        for (Method method : entity.methods) {
            methodMap.put(method.getName(), method);
            MapUtils.append(cls2methods, method.getReturnType(), method);
        }
        for (Field field : entity.fields) {
            fieldMap.put(field.getName(), field);
            MapUtils.append(cls2field, field.getType(), field);
        }
        entity.methodMap = methodMap;
        entity.fieldMap = fieldMap;
        entity.cls2field = cls2field;
        entity.cls2methods = cls2methods;
        return entity;
    }

    public static Method[] getAllMethod(Class<?> cla) {
        Method[] methods = cla.getDeclaredMethods();
        for (Method method : methods)
            method.setAccessible(true);
        return methods;
    }

    public static Field[] getAllField(Class<?> cla) {
        Field[] fields = cla.getDeclaredFields();
        for (Field method : fields)
            method.setAccessible(true);
        return fields;
    }

    private static final URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    private static Method addURLMethod;

    static {
        try {
            addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURLMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方法仅适用于 java8
     * 用作将一个jar包加载到系统 类路径
     *
     * @param file
     * @return 未知
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object addClassFileInSystem(File file) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        return addURLMethod.invoke(systemClassLoader, file.getParentFile().toURI().toURL());
    }

    /**
     * 此方法仅适用于 java8
     * 将一个含 class 文件的 文件夹 加载到 系统类路径
     *
     * @param file
     * @return 未知
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object addJarFileInSystem(File file) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        return addURLMethod.invoke(systemClassLoader, file.toURI().toURL());
    }

}
