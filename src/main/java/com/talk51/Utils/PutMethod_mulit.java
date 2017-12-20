package com.talk51.Utils;

import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;


/**
 * 发送POST请求
 *
 * @param url        目的地址
 * @param parameters 请求参数，Map类型。
 * @return 远程响应结果
 */
public class PutMethod_mulit {

    public static String sendPut(String url, Map<String, String> parameters) {
        try {
            OkHttpClient client = new OkHttpClient();
            StringBuffer sb = new StringBuffer();// 处理请求参数
            String params = "";// 编码之后的参数
            if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name)
                            .append("=")
                            .append(URLEncoder.encode(
                                    parameters.get(name), "UTF-8"));
                }
                params = sb.toString();
            } else {
                for (String name : parameters.keySet()) {
                    sb.append(name)
                            .append("=")
                            .append(URLEncoder.encode(
                                    parameters.get(name), "UTF-8")).append("&");
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
                System.out.println(params);
            }

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, params);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response;
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "system error";
        }
    }

}
