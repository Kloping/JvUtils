package io.github.kloping.initialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.object.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.kloping.file.FileUtils.*;
import static io.github.kloping.judge.Judge.isNotEmpty;
import static io.github.kloping.object.ObjectUtils.baseToPack;

/**
 * 基于文件的加载值
 *
 * @author HRS-Computer
 */
public class FileInitializeValue {

    /**
     * 从文件中获取值
     *
     * @param path     路径
     * @param defaultV 默认值
     * @param <T>
     * @return 值
     */
    public static <T extends Object> T getValue(String path, T defaultV) {
        try {
            testFile(path);
            Class<?> cla = baseToPack(defaultV.getClass());
            String str = getStringFromFile(path, "//");
            if (!isNotEmpty(str)) return defaultV;
            return toValue(str, defaultV);
        } catch (Exception e) {
            System.err.format("从%s获取%s值失败,返回%s\n", path, defaultV.getClass().getSimpleName(), defaultV);
        }
        return defaultV;
    }

    /**
     * 从文件中获取值 若 文件为空 则将默认值写入
     *
     * @param path     路径
     * @param defaultV 默认值
     * @param rePut    当为空 是否 写入默认
     * @param <T>
     * @return 值
     */
    public static <T extends Object> T getValue(String path, T defaultV, boolean rePut) {
        try {
            testFile(path);
            Class<?> cla = baseToPack(defaultV.getClass());
            String str = getStringFromFile(path, "//");
            if (!isNotEmpty(str)) {
                putStringInFile(toPar(defaultV, true), new File(path));
                return defaultV;
            }
            return toValue(str, defaultV);
        } catch (Exception e) {
            System.err.format("从%s获取%s值失败,返回%s\n", path, defaultV.getClass().getSimpleName(), defaultV);
        }
        return defaultV;
    }

    /**
     * 从文件中获取值 若 文件为空 则将tips值写入
     *
     * @param path     路径
     * @param defaultV 默认值
     * @param tips     提示
     * @param <T>
     * @return 值
     */
    public static <T extends Object> T getValue(String path, T defaultV, String tips) {
        try {
            testFile(path);
            Class<?> cla = baseToPack(defaultV.getClass());
            String str = getStringFromFile(path, "//");
            if (!isNotEmpty(str)) {
                putStringInFile("//" + tips, new File(path));
                return defaultV;
            }
            return toValue(str, defaultV);
        } catch (Exception e) {
            System.err.format("从%s获取%s值失败,返回%s\n", path, defaultV.getClass().getSimpleName(), defaultV);
        }
        return defaultV;
    }

    /**
     * 将值写入 文件
     *
     * @param path  路径
     * @param value 值
     * @param <T>
     * @return 值
     */
    public static <T extends Object> T putValues(String path, T value) {
        try {
            File file = new File(path);
            testFile(file);
            putStringInFile(toPar(value, true), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 将值写入 文件
     *
     * @param path   路径
     * @param value  值
     * @param <T>
     * @param format 是否格式化 (占用空间大)
     * @return 值
     */
    public static <T extends Object> T putValues(String path, T value, boolean format) {
        try {
            File file = new File(path);
            testFile(file);
            putStringInFile(toPar(value, format), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> void objs2list(List list, Class<T> cla) {
        List<T> ls = new ArrayList<>();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof JSONObject) {
                ls.add(((JSONObject) o).toJavaObject(cla));
            } else if (ObjectUtils.isSuperOrInterface(o.getClass(), cla)) {
                ls.add((T) o);
            }
        }
        list.clear();
        list.addAll(ls);
    }

    private static <T> T toValue(String par, T defaultV) {
        try {
            defaultV = (T) JSONObject.parseObject(par, defaultV.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultV;
    }

    private static <T> String toPar(T v) {
        try {
            Object object = JSONObject.toJSON(v);
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v.toString();
    }

    private static <T> String toPar(T v, boolean format) {
        try {
            if (!format) return toPar(v);
            String serJson = JSON.toJSONString(v, true);
            return serJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v.toString();
    }
}
