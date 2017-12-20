package com.talk51.Utils;

import okhttp3.*;

import java.io.IOException;


/**
 * 发送POST请求
 *
 * @param url        目的地址
 * @param parameters 请求参数，Map类型。
 * @return 远程响应结果
 */
public class DeleteMethod {

    public static String sendDelete(String url, String parms) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, parms);
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
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


