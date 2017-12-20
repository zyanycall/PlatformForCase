package com.borderTest;

import java.util.Set;

/**
 * 分页pageSize专用的边界测试
 */
public class PageSizeBorderParam extends IntegerBorderParam {
    PageSizeBorderParam(String paramPair, String key, String value) {
        super(paramPair, key, value);
    }

    @Override
    public Set<String> getParamList() {

        // 其它容易错的数字
        paramPairList.add(key + "=6");
        paramPairList.add(key + "=7");
        paramPairList.add(key + "=999");
        // 统计类接口page_size最大不超过999
        paramPairList.add(key + "=1000");

        return super.getParamList();
    }
}
