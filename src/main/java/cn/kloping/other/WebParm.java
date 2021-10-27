package cn.kloping.other;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class WebParm {
    public static String Cookie2JsoupParm() {
        System.out.println("Input Cookies String split ;");
        String line = new Scanner(System.in).nextLine();
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

    public static String Header2JsoupParm() {
        System.out.println("Input Header Lines by one line \"ok\" end");
        StringBuilder sb = new StringBuilder();
        String line = null;
        Scanner sc = new Scanner(System.in);
        while (!(line = sc.nextLine()).equals("ok")) {
            sb.append(line).append("\n");
        }
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

    public static String WebGetPathView() {
        System.out.println("Input WebGetPath");
        String line = new Scanner(System.in).nextLine();
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
