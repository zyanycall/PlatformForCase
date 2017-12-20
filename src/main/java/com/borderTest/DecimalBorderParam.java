package com.borderTest;

import java.util.Set;

/**
 * 小数类型的边界测试
 */
public class DecimalBorderParam extends BorderParam {


    DecimalBorderParam(String paramPair, String key, String value) {
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
        paramPairList.add(paramPair + "5");
        // 小数
        paramPairList.add(paramPair + "55");
        // 小数
        paramPairList.add(paramPair + "555");
        // 小数
        paramPairList.add(paramPair + "5555");

        return super.getParamList();
    }
}
