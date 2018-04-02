package com.borderTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * 一键参数化的父类，默认返回类型
 * 默认是字符串的校验规则
 */
public class BorderParam {

    /**
     * 公共的常量：需要校验超长变量
     */
    public final static int LONG_PARAM_NEED_CHECK = 0;
    public final static int LONG_PARAM_NO_NEED_CHECK = 1;

    /**
     * 主要用于校验入库的post请求，对一些基本的特殊字符的支持。
     */
    private final static String SPECIAL_CHARACTER_COMMON_US = "'\"&||@-- --<>,.{}[]\\_+^$#!~`";

    // 完整的一个参数段
    String paramPair = "";
    String key = "";
    String value = "";
    // 默认需要长度校验
    int needLongCheck = 0;
    // 存放替换用的修改过的参数对
    Set<String> paramPairList = new HashSet<>();

    BorderParam(String paramPair, String key, String value) {
        this.paramPair = paramPair;
        this.key = key;
        this.value = value;


    }

    // 获取被替换的参数列表，保持key=value格式，方便于替换
    public Set<String> getParamList() {
        // 默认是校验长度的。
        if (this.needLongCheck == LONG_PARAM_NEED_CHECK) {
            // 长度过长100
            paramPairList.add(paramPair + BorderCheckUtil.HTML_POINT_RED +
                    "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + BorderCheckUtil.HTML_POINT_SUFFIX);
            // 长度过长300
            paramPairList.add(paramPair + BorderCheckUtil.HTML_POINT_RED + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + BorderCheckUtil.HTML_POINT_SUFFIX);
        }

        // 默认都带不传参(key=value都不传)
        paramPairList.add(BorderCheckUtil.HTML_POINT_EMPTY);
        // 只有key值
        paramPairList.add(key + "=" + BorderCheckUtil.HTML_POINT_EMPTY);
        // 增加一些常规的英文特殊字符

        try {
            paramPairList.add(paramPair + BorderCheckUtil.HTML_POINT_RED +
                    URLEncoder.encode(SPECIAL_CHARACTER_COMMON_US, "UTF-8") +
                    BorderCheckUtil.HTML_POINT_SUFFIX);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return paramPairList;
    }

    public void setNeedLongCheck(int needLongCheck) {
        this.needLongCheck = needLongCheck;
    }
}
