package com.talk51.Utils;

import com.borderTest.BorderCheckUtil;
import okhttp3.Headers;

/**
 * 前端使用http方法的工厂类
 *
 * @author zhaoyu
 */
public class HttpMethodFactory {

    /**
     * 工厂类方法，返回http请求值
     */
    public static String getResult(String method, String url, String bodyinfo) {
        String result;
        Headers headers;
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
            case "post_body":
                headers = Headers.of("Content-Type", "application/json;charset=utf-8");
                result = HttpUtils.sendPost(headers, url, bodyinfo);
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
}
