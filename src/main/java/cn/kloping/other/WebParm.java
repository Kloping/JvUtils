package cn.kloping.other;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class WebParm {
    /**
     * 将浏览器 的 请求 Cookie 参数 分割 转为 .cookie(k,v) 的 Jsoup 语句
     * @param line line
     * @return 代码句
     */
    public static String Cookie2JsoupParm(String line) {
        String[] sss = line.split("; ?");
        Map<String, String> map = new ConcurrentHashMap<>();
        for (String s1 : sss) {
            try {
                String[] ss = s1.split("=");
                map.put(ss[0].trim(), ss[1].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(".cookie(\"").append(k).append("\",\"").append(v.replaceAll("\"", "\\\\\"")).append("\")").append("\r\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * 将浏览器 的 请求 Header 多行 参数 分割 转为 .header(k,v) 的 Jsoup 语句
     * @param line line
     * @return 代码句
     */
    public static String Header2JsoupParm(String line) {
        StringBuilder sb = new StringBuilder();
//        String line = null;
//        Scanner sc = new Scanner(System.in);
//        while (!(line = sc.nextLine()).equals("ok")) {
//            sb.append(line).append("\n");
//        }
        sb.append(line);
        String[] sss = sb.toString().split("\n");
        Map<String, String> map = new ConcurrentHashMap<>();
        for (String s1 : sss) {
            try {
                String[] ss = s1.split(": ");
                map.put(ss[0].trim(), ss[1].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.forEach((k, v) -> {
            v = v.replaceAll("\"", "\\\\\"");
            sb.append(".header(\"").append(k).append("\",\"").append(v).append("\")").append("\r\n");
        });
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * 以 and 符号 分割 get 路径 以便观察
     * @param line line
     * @return str
     */
    public static String WebGetPathView(String line) {
        if (line.contains("?")) line = line.substring(line.indexOf("?") + 1);
        String[] sss = line.split("&");
        Map<String, String> map = new ConcurrentHashMap<>();
        for (String s1 : sss) {
            try {
                String[] ss = s1.split("=");
                map.put(ss[0].trim(), ss[1].trim());
            } catch (Exception e) {
                System.err.println(s1);
            }
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append("\r\n" + k + "  =\n\t" + v);
        });
        System.out.println(sb);
        return sb.toString();
    }
}
