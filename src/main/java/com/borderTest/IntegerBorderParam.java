package com.borderTest;

import java.util.Set;

/**
 * 整数类型的边界测试
 */
public class IntegerBorderParam extends BorderParam {

    IntegerBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);

    }

    @Override
    public Set<String> getParamList() {

        // 包含非数字
        paramPairList.add(paramPair + "x");
        // 0
        paramPairList.add(key + "=0");
        // -1
        paramPairList.add(key + "=-1");
        // -9.9
        paramPairList.add(key + "=-9.9");
        // -99
        paramPairList.add(key + "=-99");
        // 小数
        paramPairList.add(paramPair + ".1");
        // 小数
        paramPairList.add(paramPair + ".10");
        // 小数
        paramPairList.add(paramPair + ".100");
        // 至少过100
        paramPairList.add(paramPair + "99");
        // 至少过1000
        paramPairList.add(paramPair + "999");
        // 至少过10000
        paramPairList.add(paramPair + "9999");
        // 超过int=2147483647
        paramPairList.add(key + "=2147483648");

        return super.getParamList();
    }
}
