package com.test;

public class test2 {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1 ; i < 101 ; i++) {
            sb.append("," + i);
        }
        System.out.println(sb);
    }

}
