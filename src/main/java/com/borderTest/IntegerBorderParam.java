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
        // 小数
        paramPairList.add(paramPair + ".1");
        // 小数
        paramPairList.add(paramPair + ".10");
        // 小数
        paramPairList.add(paramPair + ".100");

        return super.getParamList();
    }
}
