package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Node {
    int x = 0;
    int y = 0;
    int value = 0;
}

public class SuanFaBiSai1 {

    public static int[][] aa = {{-3, 6, 4, 1}, {8, -7, 5, 2}, {3, 1, 7, 4}, {-2, 8, -5, 9}};

    public static List maxSumJuZhen(Integer[][] juzhen) {

//        int aaa = juzhen[1][5];
//        java.lang.ArrayIndexOutOfBoundsException

        int maxsum = 0;
        int maxtmp = 0;

        int resultI = 0;
        int resultJ = 0;

        ArrayList result = new ArrayList();
        for (int i = 0; i < juzhen.length; i++) {
            int zhi1 = juzhen[0][i];
            // 根据他可以得到4条线。
            if (zhi1 == juzhen[juzhen.length-1][juzhen.length-1]) {
                maxsum = zhi1;
                continue;
            }

            // 第一条横向的就一条
            ArrayList list1 = new ArrayList();
            for (int j = 0; j < juzhen.length; j++) {
                list1.add(juzhen[0][j]);
            }
            // 第二条竖向也就一条
            ArrayList list2 = new ArrayList();
            for (int j = 0; j < juzhen.length; j++) {
                list2.add(juzhen[j][i]);
            }
            // 左斜线一条
            ArrayList list3 = new ArrayList();
            if (i > 0) {
                for (int j = 0; j < i + 1; j++) {
                    list3.add(juzhen[j][i - j]);
                }
            }
            // 右斜线一条
            ArrayList list4 = new ArrayList();
            for (int j = 0; j < i + 1; j++) {
                try{
                    list4.add(juzhen[j][j + i]);
                } catch (Exception e){
                    continue;
                }
            }
            // 横向的线
            ArrayList list5 = new ArrayList();
            for (int j = 0; j < juzhen.length ; j++) {
                list5.add(juzhen[i][j]);
            }
            // 补充的线
            ArrayList list6 = new ArrayList();
            for (int j = 0; j < juzhen.length; j ++) {
                try {
                    list6.add(juzhen[i+j][juzhen.length-j]);
                } catch (Exception e){
                    continue;
                }
            }

            if ((int) maxSum(list1).get(0) >= maxsum) {
                maxsum = (int) maxSum(list1).get(0);
            }
            if ((int) maxSum(list2).get(0) >= maxsum) {
                maxsum = (int) maxSum(list2).get(0);
            }
            if ((int) maxSum(list3).get(0) >= maxsum) {
                maxsum = (int) maxSum(list3).get(0);
            }
            if ((int) maxSum(list4).get(0) >= maxsum) {
                maxsum = (int) maxSum(list4).get(0);
            }
            if ((int) maxSum(list5).get(0) >= maxsum) {
                maxsum = (int) maxSum(list5).get(0);
            }
            if ((int) maxSum(list6).get(0) >= maxsum) {
                maxsum = (int) maxSum(list6).get(0);
            }

        }


        result.add(maxsum);

        return result;
    }

    public static void main(String[] args) {
        Integer[][] aaa = read();
        long start = System.currentTimeMillis();
        System.out.println(maxSumJuZhen(aaa));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    public static List maxSum(List<Integer> list) {

        int maxsum = 0;
        int maxtmp = 0;

        int resultI = 0;
        int resultJ = 0;

        for (int i = 0; i < list.size(); i++) {
            maxtmp = 0;
            for (int j = i; j < list.size(); j++) {
                maxtmp += list.get(j);
                if (maxtmp > maxsum) {
                    maxsum = maxtmp;
                    resultI = i;
                    resultJ = j;
                }
            }
        }

        ArrayList result = new ArrayList();
        result.add(maxsum);
        result.add(resultI);
        result.add(resultJ);

        return result;

    }

    public static Integer[][] read() {
        File file = new File("D:/matrix.txt");
        Integer[][] matrix = new Integer[1024][1024];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            int i = 0;
            while ((s = br.readLine()) != null) {
                String[] values = s.split(" ");
                int j = 0;
                for (String value : values) {
                    matrix[i][j++] = Integer.parseInt(value);
                }
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matrix;
    }
}
