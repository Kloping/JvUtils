package io.github.kloping.arr;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 将一组字符串 按照 指定几种正则 分割出元素
 */
public class SerializerArr {
    private Set<String> matchers = new LinkedHashSet<>();
    private Set<Pattern> patterns = new LinkedHashSet<>();

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
    public Object[] serializer(String str) {
        if (matchers.isEmpty()) return null;
        init();return start(str).toArray();
    }

    private int upsize = -1;

    private void init() {
        if (upsize == matchers.size()) return;
        patterns.clear();
        for (String mc : matchers) patterns.add(Pattern.compile(mc));
        upsize = matchers.size();
    }

    private List<Object> start(String str) {
        List<Object> list = new LinkedList<>();
        String l1 = null;
        while (!(l1 = getNearSt(str)).equals(str)) {
            int i = str.indexOf(l1);
            if (i == 0) {list.add(l1);str = str.replace(l1, "");
            } else {String s2 = str.substring(0, i);list.add(s2);list.add(l1);str = str.substring(i + l1.length());}
        }
        if (!str.isEmpty()) list.add(str);
        return list;
    }

    private String getNearSt(String str) {
        int nearst = -1;
        String maed = null;
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                String s1 = matcher.group();int i = str.indexOf(s1);
                if (nearst==-1||i < nearst) {maed = s1;nearst = i;}
            }
        }
        if (nearst != -1) return maed;
        else return str;
    }
}
