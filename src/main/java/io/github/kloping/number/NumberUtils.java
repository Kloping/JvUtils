package io.github.kloping.number;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 */
public class NumberUtils {
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
            if (char2int.containsKey(c1)) {
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

    private static final String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 阿拉伯转中文
     *
     * @param num int
     * @return str
     */
    public static String intNumber2ChineseNumber(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append(numeric[num / 1000]).append("千").
                append(numeric[num / 100 % 10] + "百").
                append(numeric[num / 10 % 10] + "十").
                append(numeric[num % 10]);
        int index = -1;
        while ((index = builder.indexOf(numeric[0], index + 1)) != -1) {
            if (index < builder.length() - 1) {
                builder.deleteCharAt(index + 1);
            }
        }
        index = 0;
        while ((index = builder.indexOf("零零", index)) != -1) {
            builder.deleteCharAt(index);
        }

        if (builder.length() > 1) {
            if (builder.indexOf(numeric[0]) == 0) {
                builder.deleteCharAt(0);
            }
            if (builder.indexOf(numeric[0]) == builder.length() - 1) {
                builder.deleteCharAt(builder.length() - 1);
            }

        }
        if (builder.indexOf("一十") == 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    public static final char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final char[] chArr = new char[]{'十', '百', '千', '万', '亿'};

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
            for (int j = 0; j < cnArr.length; j++) {
                if (c == cnArr[j]) {
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
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
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

    private static final Map<Character, Integer> char2int = new ConcurrentHashMap<>();

    static {
        int i = 1;
        for (char c : new Character[]{'一', '二', '三', '四', '五', '六', '七', '八', '九', '十'}) {
            char2int.put(c, i);
            i++;
        }
    }
}
