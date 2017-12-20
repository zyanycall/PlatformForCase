package com.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;

public class test2 {

    public static void main(String[] args) {
        String aaa = "{abc:%5B%7Bsku_type%3A'na_pri'%2Csku_count%3A28%2Cvalid_days%3A12%7D%2C%7Bsku_type%3A'na_open'%2Csku_count%3A28%2Cvalid_days%3A12%7D%5D}";

        String bbb = aaa.replaceAll("%5B", "[").replaceAll("%7B","{")
                .replaceAll("%3A",":").replaceAll("%2C",",")
                .replaceAll("%7D","}").replaceAll("%5D","]");

        JSONObject jo1 = JSONObject.fromObject(bbb);

        JSONArray ja1 = jo1.getJSONArray("abc");

        for (int i = 0 ; i < ja1.size(); i++) {
            JSONObject jo11 = ja1.getJSONObject(i);

            Iterator it = jo11.keys();
            while (it.hasNext()) {
                String ccc = it.next().toString();
                System.out.println(ccc);
                jo11.replace(ccc, "zyFix");
                System.out.println(jo1);
//                System.out.println(jo1.get("abc"));

            }
        }

        System.out.println(bbb);

    }

}
