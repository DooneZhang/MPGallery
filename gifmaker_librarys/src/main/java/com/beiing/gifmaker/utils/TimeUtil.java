package com.beiing.gifmaker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenliu on 2016/4/7.
 * 描述：日期、时间处理工具
 */
public final class TimeUtil {

    public static final String DATE_FORMAT_1 = "y年M月d日";

    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_3 = "yyyy-MM-dd";

    public static final String DATE_FORMAT_4 = "HH:mm";

    public static final String DATA_FORMAT_5 = "dd/MM";

    public static final String DATA_FORMAT_6 = "MM/yyyy";

    public static final String DATE_FORMAT_7 = "yyyy-MM";

    public static final String DATE_FORMAT_HOUR = "H";//小时
//
//    public static final String DATA_FORMAT_DAY = "dd";//天
//
//    public static final String DATE_FORMAT_MONTH = "MM";//月

    public static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";


    /**
     * @param time
     * @param format
     * @return
     */
    public static String getFormatDate(long time, String format) {
        Date d = new Date(time);
        return getFormatDate(d, format);
    }

    public static String getFormatDate(Date time, String format) {
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = sdf.format(time);
        return date;
    }

    /**
     * 返回 “上午” 或 “下午”
     *
     * @param time
     * @return
     */
    public static String getNoonString(long time) {
        Date d = new Date(time);
        return getNoonString(d);
    }


    public static String getNoonString(Date time) {
        String noon = "";
        int hour = Integer.valueOf(getFormatDate(time, DATE_FORMAT_HOUR));
        if (hour < 12) {
            noon = "上午";
        } else {
            noon = "下午";
        }
        return noon;
    }

    public static int getYear(long time) {
        Date d = new Date(time);
        return getYear(d);
    }

    public static int getYear(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(calendar.YEAR);
    }

    public static int getMonth(long time) {
        Date d = new Date(time);
        return getMonth(d);
    }

    public static int getMonth(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(long time) {
        Date d = new Date(time);
        return getDay(d);
    }

    public static int getDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取与当前时间的年份间隔
     *
     * @param smallTime
     * @return
     */
    public static int getYearGap(Long smallTime) {
        if (null == smallTime) return 0;

        int gap = getYear(System.currentTimeMillis()) - getYear(smallTime);
        return gap;
    }

    /**
     * 获取与当前时间的年份间隔
     *
     * @param smallTime
     * @return
     */
    public static int getYearGap(Date smallTime) {
        int gap = getYear(System.currentTimeMillis()) - getYear(smallTime);
        return gap;
    }


    public static Date parseDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat mat = new SimpleDateFormat(format, Locale.CHINA);
        return mat.parse(dateStr);
    }


}





