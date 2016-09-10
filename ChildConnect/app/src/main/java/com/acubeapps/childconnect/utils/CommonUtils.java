package com.acubeapps.childconnect.utils;

import java.util.Calendar;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class CommonUtils {
    public static long getStartOfTheDayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getTimeSinceStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (now - calendar.getTimeInMillis());
    }
}
