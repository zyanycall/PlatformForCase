package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Happy {
    public static void main(String args[]) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String start = "2018-12-01";
        String end = "2018-12-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> lDate = findDates(dBegin, dEnd);
        for (Date date : lDate)
        {
//            System.out.println(sdf.format(date));
            Calendar calend = Calendar.getInstance();
            calend.setTime(date);
            calend.add(Calendar.DAY_OF_MONTH, 7);
            List<Date> lllDate = findDates(date, calend.getTime());
            for (Date date111 : lllDate) {
                if (sdf.format(date).equals(sdf.format(date111))) {
                    continue;
                }
                System.out.println(sdf.format(date) + "," + sdf.format(date111));
            }
        }
    }

    public static List<Date> findDates(Date dBegin, Date dEnd)
    {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

}
