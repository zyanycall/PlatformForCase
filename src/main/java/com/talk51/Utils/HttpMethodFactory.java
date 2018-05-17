package com.talk51.Utils;

import com.borderTest.BorderCheckUtil;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;

/**
 * 前端使用http方法的工厂类
 *
 * @author zhaoyu
 */
public class HttpMethodFactory {

    // 执行时间标记的分割符
    public final static String EXECUTE_TIME_SEPARATOR = "EXECUTE_TIME_SEPARATOR";

    /**
     * 工厂类方法，返回http请求值
     */
    public static String getResult(String method, String url, String bodyinfo) {
        String result;
        Headers headers = Headers.of("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
        switch (method) {
            case "get":
                headers = Headers.of("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
                result = HttpUtils.sendGet(headers, url, bodyinfo);
                break;
            case "post":
                headers = Headers.of("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
                result = HttpUtils.sendPost(headers, url, bodyinfo);
                break;
            case "post_Json":
//                headers = Headers.of("Content-Type", "application/json;charset=utf-8");
                result = HttpUtils.sendPost4JSON(headers, url, bodyinfo);
                break;
            case "post_return_json":
                headers = Headers.of("accept", "application/json;charset=utf-8");
                result = HttpUtils.sendPost(headers, url, bodyinfo);
                break;
            case "put":
                result = HttpUtils.sendPut(url, bodyinfo);
                break;
            case "delete":
                result = HttpUtils.sendDelete(url, bodyinfo);
                break;
            default://一键边界测试进默认方法
                result = sendRequest(method, url, bodyinfo);
                break;
        }
        return result;
    }

    /**
     * 增加一个带执行时间的接口
     *
     * @return
     */
    public static String getResultWithTime(String method, String url, String bodyinfo) {
        long start = System.nanoTime();
        String result = getResult(method, url, bodyinfo);
        String executeTime = BorderCheckUtil.getExecuteTime(start);
        return executeTime + EXECUTE_TIME_SEPARATOR + result;
    }

    /**
     * 返回值的默认方法，未来可以在这里拓展
     */
    private static String sendRequest(String method, String url, String bodyinfo) {
        String result = "";
        try {
            if (method.endsWith("oneKeyCheck")) {
                // 只取前面的真正方法名
                method = method.split("@")[0];
                result = BorderCheckUtil.sendCheck(method, url, bodyinfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取返回值内容
     */
    public static String getResultData(String result) {
        if (StringUtils.isEmpty(result)
                || !result.contains(EXECUTE_TIME_SEPARATOR)
                || result.split(EXECUTE_TIME_SEPARATOR).length == 1) {
            return "";
        } else {
            String returnValue = result.split(EXECUTE_TIME_SEPARATOR)[1];
            return returnValue;
        }
    }

    /**
     * 获取返回值执行时间
     */
    public static String getResultTime(String result) {
        if (StringUtils.isEmpty(result)
                || !result.contains(EXECUTE_TIME_SEPARATOR)
                || result.split(EXECUTE_TIME_SEPARATOR).length == 1) {
            return "";
        } else {
            String returnValue = result.split(EXECUTE_TIME_SEPARATOR)[0];
            return returnValue;
        }
    }
}
