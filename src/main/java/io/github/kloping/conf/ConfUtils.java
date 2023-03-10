package io.github.kloping.conf;

import io.github.kloping.file.FileUtils;
import io.github.kloping.map.MapUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.kloping.map.MapUtils.append;
import static io.github.kloping.object.ObjectUtils.maybeType;

/**
 * @author HRS-Computer
 */
public class ConfUtils {
    /**
     * 加载 文件中 以 k=v 出现 的 键值对到 Map中
     * @param path
     * @return
     */
    public static Map<String, Object> loadConf(String path) {
        return loadConf(new File(path));
    }

    /**
     * 加载 文件中 以 k=v 出现 的 键值对到 Map中
     * @param file
     * @return
     */
    public static Map<String, Object> loadConf(File file) {
        String[] strings = FileUtils.getStringsFromFile(file.getAbsolutePath());
        Map<String, Object> map = new LinkedHashMap<>();
        for (String s1 : strings) {
            String[] ss = s1.split("=");
            if (ss.length != 2) continue;
            Object o = maybeType(ss[1]);
            map.put(ss[0], o);
        }
        return map;
    }

    /**
     * 加载 文件中 以 k=v 出现 的 键值对到 Map中
     * 此方法允许 一个键k 对 多个 值v
     *
     * @param file
     * @return
     */
    public static Map<String, List<Object>> loadConf2(File file) {
        String[] strings = FileUtils.getStringsFromFile(file.getAbsolutePath());
        Map<String, List<Object>> map = new LinkedHashMap<>();
        for (String s1 : strings) {
            String[] ss = s1.split("=");
            if (ss.length != 2) continue;
            Object o = maybeType(ss[1]);
            MapUtils.append(map, ss[0], o);
        }
        return map;
    }

    /**
     * 加载 文件中 以 k=v 出现 的 键值对到 Map中
     * 此方法允许 一个键k 对 多个 值v
     *
     * @param path
     * @return
     */
    public static Map<String, List<Object>> loadConf2(String path) {
        return loadConf2(new File(path));
    }
}
