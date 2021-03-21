package com.ivnskv.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, 1);
        return calendar.getTime();
    }

    public static Date subtractMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }
}
