package com.borderTest;

import com.talk51.Utils.HttpMethodFactory;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

/**
 * 边界测试的调用工具类
 */
public class BorderCheckUtil {

    /**
     * 私有的常量
     */
    private final static String PAGE = "page";
    private final static String SUCCESS_FLAG = "\"code\":\"10000\"";
    private final static int PARAM_TOO_LONG_100 = 100;
    private final static int PARAM_TOO_LONG_300 = 300;
    private final static int PARAM_TOO_LONG_600 = 600;
    private final static String HTML_INTERVAL_2BR = "<br><br>";

    /**
     * 共有常量
     */
    public final static String HTML_RESULT = "<font color='DarkBlue'>";
    public final static String HTML_RESULT_SUFFIX = "</font>";
    public final static String HTML_POINT_RED = "<font color='red'>";
    public final static String HTML_POINT_GREEN = "<font color='Green'>";
    public final static String HTML_POINT_SUFFIX = "</font>";

    // 空字符串的提示
    public final static String HTML_EMPTY = "(实际输入为空!)";
    public final static String HTML_POINT_EMPTY = HTML_POINT_RED + HTML_EMPTY + HTML_POINT_SUFFIX;

    // 用于随机取颜色
    public final static String[] COLORS = {"red", "Blue", "Green"};

    /**
     * 一键边界测试
     */
    public static String sendCheck(String method, String url, String bodyinfo) throws UnsupportedEncodingException {
        // 采用循环的方法，1，参数的个数在外循环，2，内循环是每个参数的变化，3，最后将变化的参数拼装回来
        // 参数前台已经转码过，所以不包含非法& = 等字符。

        // 用于收集所有返回的不同的响应"code":"10000"
        LinkedHashSet<String> errorResultList = new LinkedHashSet<>();

        // 每一个请求输出结果
        StringBuilder resultSb = new StringBuilder();
        // 各种错误返回，剔除重复的。
        StringBuilder errorResultSb = new StringBuilder();

        String params[] = bodyinfo.split("&");
        int correctParamsCount = params.length;
        int realParamsCount = 0;
        int testCount = 0;
        for (String paramPair : params) {
            // 由于数据比较规整，所以直接split方法，或者每一个参数的键值对
            try {
                // 参数值获取
                String key = paramPair.split("=")[0];
                String value = getParamValue(paramPair);

                BorderParam borderParam = getBorderParamType(paramPair, key, value, BorderParam.LONG_PARAM_NEED_CHECK);

                // 测试了多少个参数（由于肯定会走到这里，这个计数仅用作结果展示）
                realParamsCount++;
                // 对于每一个参数都有很多种边界测试
                for (String paramPairFix : borderParam.getParamList()) {
                    // 测试了多少次
                    testCount++;
                    // paramPairFix 是包含html格式的，用于结果页html展示所用。
                    // paramPairFixClear 是真正替换原有参数值，参与http请求的。
                    String paramPairFixClear = clearFixValue(paramPairFix);
                    // 替换
                    String bodyinfoFix = bodyinfo.replaceAll(paramPair, paramPairFixClear);
                    // 输出结果
                    String result = HttpMethodFactory.getResult(method, url, bodyinfoFix);

                    if (!result.contains(SUCCESS_FLAG)) {
                        // 不同错误结果的响应
                        if (!errorResultList.contains(result)) {
                            errorResultSb.append(result).append("<br>");
                            errorResultList.add(result);
                        }
                    }

                    // 返回给前台时需要解码
                    String keyDecode = URLDecoder.decode(key, "UTF-8");
                    String valueFix = getParamValue(paramPairFix);
                    String paramPairFixDecode = URLDecoder.decode(paramPairFix, "UTF-8");
                    // 定制一些特殊的返回值，返回值没有做太多的定制，因为直接返回key-value就很好
                    if (paramPairFix == HTML_POINT_EMPTY) {
                        resultSb.append(keyDecode).append(":不传");
                    } else if (valueFix.isEmpty()) {
                        resultSb.append(paramPairFix + ":传空值");
                    } else if (!valueFix.contains("%2C") && !valueFix.contains("}") && valueFix.length() > PARAM_TOO_LONG_100 && valueFix.length() < PARAM_TOO_LONG_300) {
                        resultSb.append(keyDecode + ":参数大于100字符小于300字符");
                    } else if (!valueFix.contains("%2C") && !valueFix.contains("}") && valueFix.length() > PARAM_TOO_LONG_300) {
                        resultSb.append(keyDecode).append(":参数大于300字符");
                    } else {
                        resultSb.append(paramPairFixDecode);
                    }
                    // 非正确的返回标记高识别颜色
                    if (!result.contains(SUCCESS_FLAG)) {
                        resultSb.append("<br>" + HTML_RESULT).append(result).append(HTML_RESULT_SUFFIX + HTML_INTERVAL_2BR);
                    } else {
                        resultSb.append("<br>" + HTML_POINT_GREEN).append(result).append(HTML_POINT_SUFFIX + HTML_INTERVAL_2BR);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        // 拼接结果
        String resultSbStr = resultSb.toString().replaceAll("<\\\\/font>", "</font>");

        return "参数的个数： " + correctParamsCount + "<br>" +
                "边界测试的参数个数： " + realParamsCount + "<br>" +
                "边界测试一共多少用例： " + testCount + "<br>" +
                "边界测试集合：<br>" + errorResultSb.toString() +
                "边界测试详细：<br>" + resultSbStr;
    }

    /**
     * 获取一键参数化的各种实现类
     *
     * @param paramPair key=value 组成的字符串
     * @param key       参数的key
     * @param value     参数的值
     */
    public static BorderParam getBorderParamType(String paramPair, String key, String value, int flag) {
        // 整数类型
        Pattern patternInteger = Pattern.compile("^[-\\+]?[\\d]*$");
        // YYYY-MM-DD 类型
        Pattern patternDateYY = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})$");
        // YYYY-MM-DD HH:mm:ss 类型
        Pattern patternDateHH = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})%20(\\d{2})%3A(\\d{2})%3A(\\d{2})$");

        BorderParam returnValue;

        if (key.equals("page_no")) { //page_no的校验
            returnValue = new PageNoBorderParam(paramPair, key, value);
        } else if (key.equals("page_size")) { //page_size的校验
            returnValue = new PageSizeBorderParam(paramPair, key, value);
        } else if (patternInteger.matcher(value).matches()) {
            returnValue = new IntegerBorderParam(paramPair, key, value);
        } else if (patternDateYY.matcher(value).matches()) {
            returnValue = new DateYYBorderParam(paramPair, key, value);
        } else if (patternDateHH.matcher(value).matches()) {
            returnValue = new DateHHBorderParam(paramPair, key, value);
        } else if (value.contains("%7D") || value.contains("}")) { //带}的JSON类型参数校验
            returnValue = new JSONBorderParam(paramPair, key, value);
        } else if (value.contains("%2C") || value.contains(",")) { //带英文逗号分隔的参数校验
            returnValue = new MultiBorderParam(paramPair, key, value);
        } else if (value.contains(".") || value.contains("2E")) { //小数的参数校验
            returnValue = new DecimalBorderParam(paramPair, key, value);
        } else {
            returnValue = new BorderParam(paramPair, key, value);
        }
        //当前只是一个flag，未来可以将flag类型变化为map，便于拓展。
        returnValue.setNeedLongCheck(flag);
        return returnValue;
    }

    /**
     * 取其中的value，便于被递归使用。
     *
     * @param paramPair key=value，value中没有=。
     * @return value
     */
    public static String getParamValue(String paramPair) {
        if (HTML_POINT_EMPTY.equals(paramPair)) {// 对空的标识做特殊判断
            return HTML_EMPTY;
        } else if (StringUtils.isEmpty(paramPair)
                || !paramPair.contains("=")
                || paramPair.split("=").length == 1) {
            return "";
        } else {
            String key = paramPair.split("=")[0];
            String returnValue = paramPair.replaceAll(key + "=", "");
            return returnValue;
        }
    }

    public static String clearFixValue(String valueBeforeFix) {
        String valueFix = valueBeforeFix.replaceAll(HTML_RESULT, "")
                .replaceAll(HTML_RESULT_SUFFIX, "")
                .replaceAll(HTML_POINT_RED, "")
                .replaceAll(HTML_POINT_SUFFIX, "")
                // JOSN格式会将\转换成\/，所以多一个步骤
                .replaceAll("<\\\\/font>", "")
                // 处理空值
                .replaceAll(HTML_POINT_EMPTY, "");
        return valueFix;
    }

    public static void main(String[] args) {
        System.out.println("\"\"".replaceAll("\"\"", HTML_POINT_RED + "\"(空)\"" + HTML_POINT_SUFFIX));
    }

}
