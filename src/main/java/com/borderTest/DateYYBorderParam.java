package com.borderTest;

import java.util.Set;

/**
 * YYYY-MM-DD 格式的日期边界测试
 */
public class DateYYBorderParam extends BorderParam {

    DateYYBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);
    }

    public Set<String> getParamList() {
        // 时间YY-mm-dd的校验
        // 过于未来的时间
        paramPairList.add(key + "=2020%2D01%2D01");
        // 过早的时间
        paramPairList.add(key + "=1991%2D01%2D01");
        // 非法的日期
        paramPairList.add(key + "=" + value.substring(0, value.length() - 2) + "32");
        // 时分秒格式
        paramPairList.add(key + "=" + value + "%2000%3A00%3A00");
        return super.getParamList();
    }
}
