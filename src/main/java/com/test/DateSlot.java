package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zyanycall@gmail.com on 2019/4/9 11:18.
 */
public class DateSlot {
    public static void main(String args[]) throws ParseException {
        String start = "2019-04-08";
        String end = "2019-04-14";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfWithout = new SimpleDateFormat("yyyyMMdd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> lDate = Happy.findDates(dBegin, dEnd);
        for (Date date : lDate)
        {
//            System.out.println(sdfWithout.format(date));
            for (int i = 37 ; i <= 42 ; i++) {
                System.out.println(sdfWithout.format(date) + "_" + i);
            }
        }
    }
}
