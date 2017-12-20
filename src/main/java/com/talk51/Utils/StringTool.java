package com.talk51.Utils;

import java.io.UnsupportedEncodingException;

public class StringTool {
    public static String toHexString(String s) {
        if (s == null) {
            return "";
        } else {
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                int ch = (int) s.charAt(i);
                String s4 = Integer.toHexString(ch);
                if (s4.length() == 1) {
                    s4 = "000" + s4;
                } else if (s4.length() == 2) {
                    s4 = "00" + s4;
                } else if (s4.length() == 3) {
                    s4 = "0" + s4;
                }
                str = str + s4;
            }
            return str;
        }
    }

    public static String toStingHex(String s) throws UnsupportedEncodingException {
        String str = "";
        for (int i = 0; i < s.length(); i = i + 4) {
            String buffresult = "\\u" + s.substring(i, i + 4);
            buffresult = decodeUnicode(buffresult);
            str = str + buffresult;
        }
        return str;
    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
