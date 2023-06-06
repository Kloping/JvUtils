package io.github.kloping.arr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 将 String 按照指定 Rule 转为 集合 或数组
 */
public class ArrDeSerializer<A> {
    public static final Pattern EMPTY_PATTERN = Pattern.compile("");

    /**
     * 转换规则
     *
     * @param <T>
     */
    private abstract class Rule<T extends A> {
        private Pattern pattern;

        public abstract T deserializer(String s);

        public Rule(Pattern pattern) {
            this.pattern = pattern;
        }
    }

    public interface Rule0<T> {
        T deserializer(String s);
    }

    public ArrDeSerializer() {
    }

    /**
     * 添加一个转换规则
     *
     * @param pattern
     * @param rule0
     * @return
     */
    public <T extends A> ArrDeSerializer add(Pattern pattern, Rule0<T> rule0) {
        Rule rule = new Rule(pattern) {
            @Override
            public A deserializer(String s) {
                return rule0.deserializer(s);
            }
        };
        p2r.put(rule.pattern, rule);
        return this;
    }

    private Map<Pattern, Rule> p2r = new HashMap<>();

    /**
     * 开始转换
     *
     * @param s
     * @return
     */
    public List<A> deserializer(String s) {
        List<A> list = new LinkedList<>();
        w0(s, list);
        return list;
    }

    private SerializerArr sa = new SerializerArr();

    private void w0(String s, List<A> list) {
        for (Rule rule : p2r.values()) {
            if (rule.pattern == EMPTY_PATTERN) continue;
            sa.add(rule.pattern.pattern());
        }
        while (true) {
            if (s.length() == 0) break;
            s = w1(s, list);
        }
    }

    private Pattern getPattern(String s) {
        for (Rule rule : p2r.values()) {
            if (rule.pattern.matcher(s).matches()) return rule.pattern;
        }
        return null;
    }

    private String w1(String s, List<A> list) {
        for (String str : sa.serializer(s)) {
            Pattern pattern = getPattern(str);
            if (pattern == null) pattern = EMPTY_PATTERN;
            A a = (A) p2r.get(pattern).deserializer(str);
            list.add(a);
            s = s.replace(str, "");
        }
        return s;
    }
}
