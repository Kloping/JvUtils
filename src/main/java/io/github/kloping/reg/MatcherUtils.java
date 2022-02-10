package io.github.kloping.reg;

import io.github.kloping.judge.Judge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author github-kloping
 */
public class MatcherUtils {
    /**
     * 获取所有匹配正则的 结果
     *
     * @param oStr 源字符串
     * @param regx 正则
     * @return 结果集
     */
    public static String[] matcherAll(String oStr, String regx) {
        if (Judge.isEmpty(oStr, regx)) return null;
        Matcher matcher = Pattern.compile(regx).matcher(oStr);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list.toArray(new String[0]);
    }
}
