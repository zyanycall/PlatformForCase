package com.borderTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.talk51.Utils.HttpMethodFactory;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * 边界测试的调用工具类
 */
public class BorderCheckUtil {

    /**
     * 私有的常量
     */
    private final static String PAGE = "page";
    private final static String SUCCESS_FLAG = "\"10000\"";
    private final static int PARAM_TOO_LONG_100 = 100;
    private final static int PARAM_TOO_LONG_300 = 300;
    private final static int PARAM_TOO_LONG_600 = 600;
    private final static String HTML_INTERVAL_2BR = "<br><br>";
    private final static int RESULT_TOO_LONG_2000 = 2000;

    /**
     * 共有常量
     */
    public final static String HTML_RESULT = "<span style='white-space: pre-wrap;'><font color='DarkBlue'>";
    public final static String HTML_RESULT_SUFFIX = "</font></span>";
    public final static String HTML_POINT_RED = "<font color='red'>";
    public final static String HTML_POINT_GREEN = "<span style='white-space: pre-wrap;'><font color='Green'>";
    public final static String HTML_POINT_SUFFIX = "</font></span>";

    // 为了JSON内字符串的替换
    public final static String HTML_POINT_SUFFIX_4JSON = "<\\/font><\\/span>";

    // 空字符串的提示
    public final static String HTML_EMPTY = "实际输入为空!";
    public final static String HTML_POINT_EMPTY = HTML_POINT_RED + HTML_EMPTY + HTML_POINT_SUFFIX;

    // key和value都不传的提示，目前只针对Json格式参数，其它都是打印结果集时替换的。
    public final static String HTML_NO_KEY_NO_VALUE = "不传！";

    // 用于随机取颜色
    public final static String[] COLORS = {"red", "Blue", "Green"};

    // 执行时间超过0.5秒要标红提示
    public final static double EXECUTE_TIME_LONG = 0.5;

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
        // 打印开始结束时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = df.format(new Date());
        // 各种错误返回，剔除重复的。
        StringBuilder errorResultSb = new StringBuilder();
        // 如果遇到http访问异常，参数的信息。
        StringBuilder exceptionResultSb = new StringBuilder();

        // 如果遇到超长的异常信息如SQL异常，生成异常提示集合避免重复导致字段过长页面显示失败
        TreeMap<String, String> tooLongResultMap = new TreeMap<>();
        int tooLongResultCount = 0;

        String params[] = bodyinfo.split("&");
        int correctParamsCount = params.length;
        int realParamsCount = 0;
        int testCount = 0;
        for (String paramPair : params) {

            // 由于数据比较规整，所以直接split方法，或者每一个参数的键值对
            // 参数值获取
            String key = paramPair.split("=")[0];
            String value = getParamValue(paramPair);
            try {
                BorderParam borderParam = getBorderParamType(paramPair, key, value, BorderParam.LONG_PARAM_NEED_CHECK);

                // 测试了多少个参数（由于肯定会走到这里，这个计数仅用作结果展示）
                realParamsCount++;
                // 对于每一个参数都有很多种边界测试
                for (String paramPairFix : borderParam.getParamList()) {
                    // 测试了多少次
                    testCount++;
                    // paramPairFix 是包含html格式的，用于结果页html展示所用。
                    // paramPairFixClear 是真正替换原有参数值，去掉了html格式，参与http请求的。
                    String paramPairFixClear = clearFixValue(paramPairFix);
                    // 替换
                    String bodyinfoFix = "";
                    if (bodyinfo.endsWith(paramPair)) {
                        bodyinfoFix = bodyinfo.replace("&" + paramPair, "&" + paramPairFixClear);
                    } else {
                        bodyinfoFix = bodyinfo.replace(paramPair + "&", paramPairFixClear + "&");
                    }
                    // 输出结果(非Json格式)
                    String resultAndTime = HttpMethodFactory.getResultWithTime(method, url, bodyinfoFix);
                    String noJsonResult = HttpMethodFactory.getResultData(resultAndTime);

                    String result = toJson(noJsonResult);
                    if (!noJsonResult.contains(SUCCESS_FLAG)) {
                        // 对超长的不得不显示的异常提示的处理
                        if (noJsonResult.length() > RESULT_TOO_LONG_2000) {
                            if (tooLongResultMap.containsKey(result)) {
                                result = "<a href='#" + tooLongResultMap.get(result) + "'>" + tooLongResultMap.get(result) + "</a>";
                            } else {
                                String tooLongResultSingle = "返回超长已做唯一化，请点击链接查看：" + tooLongResultCount++;
                                tooLongResultMap.put(result, tooLongResultSingle);
                                result = "<a href='#" + tooLongResultSingle + "'>" + tooLongResultSingle + "</a>";
                            }
                        }
                        // 不同错误结果的响应
                        if (!errorResultList.contains(noJsonResult)) {
                            errorResultSb.append(noJsonResult).append("<br>");
                            errorResultList.add(noJsonResult);
                        }
                    }

                    // 输出执行时间(超过限定时间标红)
                    resultSb.append(HttpMethodFactory.getResultTime(resultAndTime));

                    // 返回给前台时需要解码
                    String keyDecode = URLDecoder.decode(key, "UTF-8");
                    String valueFix = getParamValue(paramPairFix);
                    String paramPairFixDecode = URLDecoder.decode(paramPairFix, "UTF-8");
                    // 定制一些特殊的返回值，返回值没有做太多的定制，因为直接返回key-value就很好
                    if (paramPairFix.equals(HTML_POINT_EMPTY)) {
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
                exceptionResultSb.append(key + "<br>");
                continue;
            }
        }
        String endTime = df.format(new Date());
        // 拼接结果
        String resultSbStr = resultSb.toString().replace("<\\/font>", "</font>");

        String returnValue = "参数的个数： " + correctParamsCount + "<br>" +
                "边界测试的参数个数： " + realParamsCount + "<br>" +
                "运行时异常：" + (exceptionResultSb.toString().isEmpty() ? "<无><br>" : HTML_POINT_RED + exceptionResultSb + HTML_POINT_SUFFIX) +
                "边界测试用例个数： " + testCount + "<br>" +
                "边界测试开始时间：" + beginTime + "<br>" +
                "边界测试结束时间：" + endTime + "<br>" +
                "边界测试提示集合(个)：" + errorResultList.size() + " <br>" + errorResultSb.toString() +
                "边界测试详细：<br>" + resultSbStr;

        if (tooLongResultMap.size() > 0) {
            String tooLongResult = "";
            for (String key : tooLongResultMap.keySet()) {
                tooLongResult += HTML_INTERVAL_2BR + HTML_POINT_RED + "<a name='" + tooLongResultMap.get(key) + "'>" +
                        tooLongResultMap.get(key) + "</a>" + HTML_POINT_SUFFIX +
                        HTML_INTERVAL_2BR + HTML_RESULT + key + HTML_RESULT_SUFFIX;
            }
            return returnValue + tooLongResult;
        }
        return returnValue;

    }

    /**
     * 获取一键参数化的各种实现类
     *
     * @param paramPair key=value 组成的字符串
     * @param key       参数的key
     * @param value     参数的值
     * @param flag      是否需要超长参数的校验
     */
    public static BorderParam getBorderParamType(String paramPair, String key, String value, int flag) {
        // 整数类型
        Pattern patternInteger = Pattern.compile("^[-\\+]?[\\d]*$");
        // YYYY-MM-DD 类型
        Pattern patternDateYY = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})$");
        // YYYY-MM-DD HH:mm:ss 类型
        Pattern patternDateHH = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})%20(\\d{2})%3A(\\d{2})%3A(\\d{2})$");

        BorderParam returnValue;

        if (key.equalsIgnoreCase("page_no")) { //page_no的校验
            returnValue = new PageNoBorderParam(paramPair, key, value);
        } else if (key.equalsIgnoreCase("page_size")) { //page_size的校验
            returnValue = new PageSizeBorderParam(paramPair, key, value);
        } else if (patternInteger.matcher(value).matches()) {// 整数类型判断
            returnValue = new IntegerBorderParam(paramPair, key, value);
        } else if (patternDateYY.matcher(value).matches()) {// YYYY-MM-DD 类型判断
            returnValue = new DateYYBorderParam(paramPair, key, value);
        } else if (patternDateHH.matcher(value).matches()) {// YYYY-MM-DD HH:mm:ss 类型判断
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
        if (HTML_POINT_EMPTY.equalsIgnoreCase(paramPair)) {// 对key和value都不传的情况做判断
            return HTML_NO_KEY_NO_VALUE;
        } else if (StringUtils.isEmpty(paramPair)
                || !paramPair.contains("=")
                || paramPair.split("=").length == 1) {
            return "";
        } else {
            String key = paramPair.split("=")[0];
            String returnValue = paramPair.replace(key + "=", "");
            return returnValue;
        }
    }

    /**
     * 消除自定义的Html格式信息
     *
     * @param valueBeforeFix 修复之前的
     * @return 无自定义Html格式字符串
     */
    public static String clearFixValue(String valueBeforeFix) {
        String valueFix = valueBeforeFix
                // 处理空值
                .replace(HTML_RESULT, "")
                .replace(HTML_RESULT_SUFFIX, "")
                .replace(HTML_POINT_RED, "")
                .replace("</font>", "")
                .replace("</span>", "")
                // JSON格式会将\转换成\/，所以多几个步骤
                .replace("<\\/font>", "")
                .replace("<\\/span>", "")
                .replace(HTML_EMPTY, "");
        return valueFix;
    }

    /**
     * 将数据Json格式化
     *
     * @param noJsonResult 无格式字符串
     * @return json format result
     */
    public static String toJson(String noJsonResult) {
        String result;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            result = gson.toJson(jp.parse(noJsonResult));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            result = noJsonResult;
        }
        return result;
    }

    /**
     * 返回执行时间，带有HTML标红格式
     */
    public static String getExecuteTime(long start) {
        double executeTime = (System.nanoTime() - start) * 0.000000001;
        String executeTime4Show = String.format("%.4f", executeTime);
        if (executeTime > EXECUTE_TIME_LONG) {
            return "执行时间:" + HTML_POINT_RED + executeTime4Show + "秒" + HTML_POINT_SUFFIX + "<br>";
        }
        return "执行时间:" + executeTime4Show + "秒<br>";
    }

    public static void main(String[] args) {

//        System.out.println("class_info=[{\"src_class_id\":80349718,\"tar_class_id\":80350002,\"tar_class_room_id\":187202},{\"src_class_id\":80349789,\"tar_class_id\":80350073}]&stu_id=606492200&admin_id=1234444&reason=zyTest001&appkey=om_backend&timestamp=1517797971"
//                .replace("class_info=[{\"src_class_id\":80349718,\"tar_class_id\":80350002,\"tar_class_room_id\":187202},{\"src_class_id\":80349789,\"tar_class_id\":80350073}]&",
//                        "class_info=[{\"src_class_id\":\"80349718.1\",\"tar_class_id\":80350002,\"tar_class_room_id\":187202},{\"src_class_id\":80349789,\"tar_class_id\":80350073}]&"));

    }

}
