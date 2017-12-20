package com.borderTest;

import java.util.Set;

/**
 * YYYY-MM-DD HH:mm:ss 格式的日期边界测试
 */
public class DateHHBorderParam extends BorderParam {
    DateHHBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);
    }

    public Set<String> getParamList() {
        // 时间YY-mm-dd hh:mm:ss的校验
        // - %2D  : %3A
        // 过于未来的时间
        paramPairList.add(key + "=2020%2D01%2D01%2000%3A00%3A00");
        // 过早的时间
        paramPairList.add(key + "=1991%2D01%2D01%2000%3A00%3A00");
        // 非法的日期
        paramPairList.add(key + "=" + value.replaceAll("\\d{2}%20", "32%20"));
        // 非法的时间
        paramPairList.add(key + "=" + value.substring(0, value.length() - 2) + "61");
        // 仅传入日期
        paramPairList.add(key + "=" + value.split("%20")[0]);
        return super.getParamList();
    }
}
