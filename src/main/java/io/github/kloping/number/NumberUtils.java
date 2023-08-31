package io.github.kloping.number;

import io.github.kloping.judge.Judge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 */
public class NumberUtils {
    private static final String[] SS1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final char[] CS1 = {'十', '百', '千', '万', '亿'};
    public static final char[] CS0 = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final Map<Character, Integer> CHAR_2_INT = new ConcurrentHashMap<>();

    static {
        int i = 1;
        for (char c : new Character[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '十'}) {
            CHAR_2_INT.put(c, i);
            i++;
        }
    }

    /**
     * 从字符串获取 整数对象
     * 若无则 null
     *
     * @param str
     * @return
     */
    public static Integer getIntegerFromString(String str) {
        return getIntegerFromString(str, null);
    }

    /**
     * 从字符串获取 整数对象
     * 若无则 def
     *
     * @param str
     * @param def
     * @return
     */
    public static Integer getIntegerFromString(String str, Integer def) {
        String ns = findNumberFromString(str);
        if (Judge.isEmpty(ns)) {
            return def;
        } else {
            return Integer.parseInt(ns);
        }
    }

    /**
     * 从字符串中发现阿拉伯数字
     *
     * @param str str
     * @return str
     */
    public static String findNumberFromString(String str) {
        String ss = "";
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                int c = str.codePointAt(i);
                if (c >= '0' && c <= '9') {
                    ss += (char) c;
                }
            }
        }
        return ss;
    }

    /**
     * 计算百分比
     *
     * @param b b%
     * @param v 值
     * @return v 的x b%
     */
    public static Long percentTo(Integer b, Number v) {
        if (v.longValue() < 100) {
            float f = b / 100f;
            return (long) (f * (v.intValue()));
        }
        double d = v.longValue();
        d /= 100;
        d *= b;
        long v1 = (long) d;
        return v1;
    }

    /**
     * @param v1 v1
     * @param v2 v2
     * @return v1/v2 到 %
     */
    public static Integer toPercent(Number v1, Number v2) {
        double dv1 = (double) v1.longValue();
        double dv2 = (double) v2.longValue();
        double dv3 = dv1 / dv2;
        dv3 *= 100;
        int v3 = (int) dv3;
        return v3;
    }
    /**
     * 从字符串中发现 中文数字
     *
     * @param str str
     * @return str
     */
    public static String findNumberZh(String str) {
        List<Character> cs = new ArrayList<>();
        char[] cs1 = str.toCharArray();
        for (char c1 : cs1) {
            if (CHAR_2_INT.containsKey(c1)) {
                cs.add(c1);
                continue;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 阿拉伯转中文
     *
     * @param num int
     * @return str
     */
    public static String intNumber2ChineseNumber(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append(SS1[num / 1000]).append("千").
                append(SS1[num / 100 % 10] + "百").
                append(SS1[num / 10 % 10] + "十").
                append(SS1[num % 10]);
        int index = -1;
        while ((index = builder.indexOf(SS1[0], index + 1)) != -1) {
            if (index < builder.length() - 1) {
                builder.deleteCharAt(index + 1);
            }
        }
        index = 0;
        while ((index = builder.indexOf("零零", index)) != -1) {
            builder.deleteCharAt(index);
        }

        if (builder.length() > 1) {
            if (builder.indexOf(SS1[0]) == 0) {
                builder.deleteCharAt(0);
            }
            if (builder.indexOf(SS1[0]) == builder.length() - 1) {
                builder.deleteCharAt(builder.length() - 1);
            }

        }
        if (builder.indexOf("一十") == 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 中文数字转阿拉伯
     *
     * @param chineseNumber str
     * @return int
     */
    public static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;
        int count = 0;
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < CS0.length; j++) {
                if (c == CS0[j]) {
                    if (0 != count) {
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {
                for (int j = 0; j < CS1.length; j++) {
                    if (c == CS1[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {
                result += temp;
            }
        }
        return result;
    }

    /**
     * 将l除以v 保留 d 位小数
     *
     * @param l
     * @param v
     * @param d
     * @return
     */
    public String device(Long l, double v, int d) {
        double f = l / v;
        return String.format("%." + d + "f", f);
    }
}
