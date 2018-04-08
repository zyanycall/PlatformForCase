package com.talk51.Utils;

import okhttp3.*;

import java.io.IOException;

public class HttpUtils {

    private static class Singleton {
        private static OkHttpClient client = new OkHttpClient();
    }

    public synchronized static OkHttpClient getOkHttpClient() {
        return Singleton.client;
    }

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

    public static String sendPost(Headers headers, String url, String params) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, params);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build();

            Response response = getOkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

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
