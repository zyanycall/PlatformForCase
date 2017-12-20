package com.talk51.Utils;

public class GenTool {
    public static String GetTimeGenId() {
        return Long.toString(System.currentTimeMillis()).substring(Long.toString(System.currentTimeMillis()).length() - 8, Long.toString(System.currentTimeMillis()).length());
    }
}
