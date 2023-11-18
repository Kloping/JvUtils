package io.github.kloping.arr;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 将一组字符串 按照 指定几种正则 分割出元素
 */
public class SerializerArr {
    private Set<String> matchers = new HashSet<>();
    private Set<Pattern> patterns = new HashSet<>();

    /**
     * 添加一个正则
     * @param matcher
     * @return
     */
    public boolean add(String matcher) {
        try {Pattern.compile(matcher);}
        catch (PatternSyntaxException e) {return false;}
        return matchers.add(matcher);
    }

    /**
     * 开始分割
     * @param str
     * @return
     */
    public String[] serializer(String str) {
        if (matchers.isEmpty()) return null;
        init();
        return start(str).toArray(new String[0]);
    }

    private int upsize = -1;

    private void init() {
        if (upsize == matchers.size()) return;
        patterns.clear();
        for (String mc : matchers) patterns.add(Pattern.compile(mc));
        upsize = matchers.size();
    }

    private List<String> start(String str) {
        List<String> list = new LinkedList<>();
        String l1 = null;
        while (!(l1 = getNearSt(str)).equals(str)) {
            int i = str.indexOf(l1);
            if (i == 0) {
                list.add(l1);
                str = str.substring(l1.length());
            } else {
                String s2 = str.substring(0, i);
                list.add(s2);
                list.add(l1);
                str = str.substring(i + l1.length());
            }
        }
        if (!str.isEmpty()) list.add(str);
        return list;
    }

    private String getNearSt(String str) {
        int nearst = -1;
        String maed = null;
        Iterator<Pattern> iterator = patterns.iterator();
        while (iterator.hasNext()) {
            Matcher matcher = iterator.next().matcher(str);
            if (matcher.find()) {
                String s1 = matcher.group();
                int i = str.indexOf(s1);
                if (nearst == -1 || i < nearst) {
                    maed = s1;
                    nearst = i;
                }
            }
        }
        if (nearst != -1) return maed;
        else return str;
    }
}
