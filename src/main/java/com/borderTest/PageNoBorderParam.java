package com.borderTest;

import java.util.Set;

/**
 * 分页pageNo专用的边界测试
 */
public class PageNoBorderParam extends IntegerBorderParam {
    PageNoBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);
    }

    @Override
    public Set<String> getParamList() {

        // 其它容易错的数字
        paramPairList.add(key + "=99");

        return super.getParamList();
    }
}