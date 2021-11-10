package cn.kloping.string;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringUtils {
    /**
     * 过滤数组中 以 str 开头的 元素
     *
     * @param sss 原数组
     * @param str str
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
     * @param sss 原数组
     * @return 处理后的
     */
    public static String connectStrings(String... sss) {
        StringBuilder sb = new StringBuilder();
        for (String s : sss) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 所有字符串之前加
     *
     * @param with
     * @param sss
     * @return
     */
    public static String[] appendAll(String with, String... sss) {
        for (int i = 0; i < sss.length; i++) {
            sss[i] = with + sss[i];
        }
        return sss;
    }

    /**
     * 将某字符串 去除一次指定的 一些字符
     * @param oStr 源字符串
     * @param ss 要去除的
     * @return 处理完的字符串
     */
    public static String removeStr(String oStr, String... ss) {
        for (String s : ss)
            oStr = oStr.replace(s, "");
        return oStr;
    }
}
