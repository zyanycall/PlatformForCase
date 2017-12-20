package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoyu on 2017/6/17.
 */
public class Happy {
    public static void main(String args[]) {

        readFileByLines("E:\\jmeter_workspace\\html\\TestReport201709200304_copy.txt");

    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
//            StringBuilder sb = new StringBuilder();
            String reg = "\"class_id\":\"[0-9]+\"},\"message\":\"success\",\"code\":\"10000\"}";
            String reg1 = "[0-9]{8}";
            Pattern p = Pattern.compile(reg);
            Pattern p1 = Pattern.compile(reg1);
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                sb.append(tempString);
//                System.out.println("line " + line + ": " + tempString);
                Matcher m = p.matcher(tempString);
                if(m.find()){
                    String rawData = m.group(0);
                    Matcher m1 = p1.matcher(rawData);
                    if(m1.find()){
                        String rawData1 = m1.group(0);
                        System.out.println(rawData1);  // 组提取字符串 0x993902CE
                    }
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


}
