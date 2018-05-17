package com.talk51.Utils;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP的方法集中处理
 */
public class HttpUtils {

    /**
     * 是实现client的单例
     */
    private static class Singleton {
        private final static int CONNECT_TIMEOUT = 60;
        private final static int READ_TIMEOUT = 100;
        private final static int WRITE_TIMEOUT = 60;

        private static OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    public synchronized static OkHttpClient getOkHttpClient() {
        return Singleton.client;
    }

    /**
     * get方法
     */
    public static String sendGet(Headers headers, String url, String params) {
        try {
            Request request = new Request.Builder()
                    .url(url + "?" + params)
                    .get()
                    .headers(headers)
                    .build();
            Response response = getOkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * post方法
     */
    public static String sendPost(Headers headers, String url, String params) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            return getPostRsp(headers, url, params, mediaType);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * post_4JSON方法
     */
    public static String sendPost4JSON(Headers headers, String url, String params) {
        try {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            return getPostRsp(headers, url, params, mediaType);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String getPostRsp(Headers headers, String url, String params, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();

        Response response = getOkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    /**
     * put方法
     */
    public static String sendPut(String url, String parms) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, parms);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = getOkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * delete方法
     */
    public static String sendDelete(String url, String parms) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, parms);
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = getOkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}
