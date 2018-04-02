package com.talk51.Utils;

import com.borderTest.BorderCheckUtil;

import java.util.HashMap;

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
        HashMap headersJsonMap = new HashMap();
        switch (method) {
            case "get":
                result = GetMethod.sendGet(url, bodyinfo);
                break;
            case "post":
                result = PostUtil.sendPost(null, url, bodyinfo);
                break;
            case "post_body":
                headersJsonMap.put("Content-Type", "application/json;charset=utf-8");
                result = PostUtil.sendPost(headersJsonMap, url, bodyinfo);
                break;
            case "post_return_json":
                headersJsonMap.put("accept", "application/json");
                result = PostUtil.sendPost(headersJsonMap, url, bodyinfo);
                break;
            case "put":
                result = PutMethod.sendPut(url, bodyinfo);
                break;
            case "delete":
                result = DeleteMethod.sendDelete(url, bodyinfo);
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
