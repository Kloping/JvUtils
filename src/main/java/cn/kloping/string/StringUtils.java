package cn.kloping.string;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringUtils {
    /**
     * 过滤数组中 以 str 开头的 元素
     *
     * @param sss
     * @param str
     * @return 处理后的
     */
    public static String[] filterStartWith(String[] sss, String str) {
        List<String> list = new CopyOnWriteArrayList<>();
        for (String s : sss) {
            if (s.startsWith(str)) continue;
            list.add(s);
        }
        return list.toArray(new String[0]);
    }

    /**
     * 将一串 String 数组 连接成一个 String
     *
     * @param sss
     * @return 处理后的
     */
    public static String connectStrings(String... sss) {
        StringBuilder sb = new StringBuilder();
        for (String s : sss) {
            sb.append(s);
        }
        return sb.toString();
    }
}
