package com.talk51.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送GET请求
 *
 * @return 远程响应结果
 */

public class GetMethod_Old {

    public static String sendGet(String url, Map<String, String> param) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        StringBuffer sb = new StringBuffer();// 存储参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (param.size() == 0) {
                params = "";
            } else if (param.size() == 1) {
                for (String name : param.keySet()) {
                    sb.append(name)
                            .append("=")
                            .append(URLEncoder.encode(
                                    param.get(name), "UTF-8"));
                    params = sb.toString();
                }
            } else {
                for (String name : param.keySet()) {
                    sb.append(name)
                            .append("=")
                            .append(URLEncoder.encode(
                                    param.get(name), "UTF-8")).append("&");
                    String temp_params = sb.toString();
                    params = temp_params.substring(0, temp_params.length() - 1);
                }
            }
            String full_url = url + "?" + params;
            System.out.println(full_url);
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(full_url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 建立实际的连接
            httpConn.connect();
            // 响应头部获取
            Map<String, List<String>> headers = httpConn.getHeaderFields();
            // 遍历所有的响应头字段
//			for (String key : headers.keySet()) {
//				System.out.println(key + "\t：\t" + headers.get(key));
//			}
            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式
            in = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) {
        String str = "http://172.16.16.53";
        String str2 = "http://172.16.0.71:8081";
        String str3 = "http://www.51talk.com";


        getIpPort(str);
        getIpPort(str2);
        getIpPort(str3);


    }

    public static void getIpPort(String str) {
        String regex = "http://(.*?):(.*)";
        if (!str.matches(regex)) {
            regex = "//(.*?)(.*)";
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        while (m.find()) {
            System.out.println(m.group(1) == null ? "null" : m.group(1));
            System.out.println(m.group(2) == null ? "null" : m.group(2));
        }

    }
}
