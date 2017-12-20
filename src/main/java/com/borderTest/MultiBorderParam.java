package com.borderTest;

import java.util.Set;

/**
 * 多个参数逗号分隔的边界测试
 */
public class MultiBorderParam extends IntegerBorderParam {

    MultiBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);
    }

    @Override
    public Set<String> getParamList() {

        // 多逗号相关的边界校验  , : %2C
        paramPairList.add(paramPair + "%2C%2C%2C");
        // 参数中带有负数
        paramPairList.add(paramPair + "%2C-1");
        // 多ID查询超过100个(增加100个)
        paramPairList.add(paramPair + "1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10");
        // 多ID查询超过200个(增加200个)
        paramPairList.add(paramPair + "1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10");

        // 多ID查询超过300个(增加300个)
        paramPairList.add(paramPair + "1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10" +
                "%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10");

        return super.getParamList();
    }
}
