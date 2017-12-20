package com.talk51.Utils;

public class ToolForRandomParmRandomUnit {
    public static String randomparm(int unit_int_part_random, int unit_dec_part_random) {
        boolean isdec;

        if (unit_dec_part_random == 0) {
            isdec = false;
        } else {
            isdec = true;
        }


        String result_int_part = "";
        String result_dec_part = "";
        String result = "";

        int unit_int_part_random_f = (int) (Math.random() * (unit_int_part_random + 1 - 1) + 1);
        int unit_dec_part_random_f = (int) (Math.random() * (unit_dec_part_random + 1 - 1) + 1);
        if (unit_int_part_random == 0) {
            unit_int_part_random_f = 0;
        }

        if (unit_int_part_random_f == 1) {
            result_int_part = Integer.toString((int) (1 + Math.random() * (9 + 1) - 1));//0-9
        } else if (unit_int_part_random_f == 0) {
            result_int_part = "0";
        } else {
            int[] temp_int_part = new int[unit_int_part_random_f];
            for (int i = 0; i < unit_int_part_random_f; i++) {
                if (i == 0) {
                    temp_int_part[i] = (int) (Math.random() * (9 - 1) + 1);//1-9
                } else {
                    temp_int_part[i] = (int) (Math.random() * (9 + 1) - 1);//0-9
                }
            }
            String parm_int_part = Integer.toString(temp_int_part[0]);
            for (int j = 1; j <= unit_int_part_random_f - 1; j++) {
                parm_int_part = parm_int_part + Integer.toString(temp_int_part[j]);
            }
            result_int_part = parm_int_part;
        }
        int[] temp_dec_part = new int[unit_dec_part_random_f];
        for (int i = 0; i < unit_dec_part_random_f; i++) {
            temp_dec_part[i] = (int) (Math.random() * (9 - 1 + 1));
        }
        String parm_dec_part = Integer.toString(temp_dec_part[0]);
        for (int j = 1; j <= unit_dec_part_random_f - 1; j++) {
            parm_dec_part = parm_dec_part + Integer.toString(temp_dec_part[j]);
        }
        result_dec_part = parm_dec_part;

        if (isdec) {
            result = result_int_part + "." + result_dec_part;
        } else {
            result = result_int_part;
        }
        return result;
    }
}
