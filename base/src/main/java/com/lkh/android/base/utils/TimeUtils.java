package com.lkh.android.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luokanghui
 * @time 2015-2-11 16:52
 */
public class TimeUtils {
    /**
     * 指定格式返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间(格式以HH:mm形式)
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * 得到当前时间
     * @return yyyy-MM-dd HH:mm:ss 这种形式
     */
    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return time ;
    }
}
