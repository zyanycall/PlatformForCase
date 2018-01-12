package com.talk51.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 发送POST请求
 *
 * @return 远程响应结果
 */
public class PostUtil {

    public static String sendPost(Map<String, String> headersMap, String url, String params) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer();// 处理请求参数
        //输出异常
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            // 创建URL对象
            URL connURL = new URL(url);
            // 打开URL连接
            HttpURLConnection httpConn = (HttpURLConnection) connURL
                    .openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            if (headersMap != null) {
                for (String headerMapKey : headersMap.keySet()) {
                    httpConn.setRequestProperty(headerMapKey,
                            headersMap.get(headerMapKey));
                }
            }
            httpConn.setUseCaches(true);
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(params);
            //	System.out.println(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            e.printStackTrace(pw);
            return e.getMessage();
        } finally {
            pw.close();
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                sw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }
}
