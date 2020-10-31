package com.nullptr.utils.time;


import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间解析工具类
 *
 * @author majl
 * @version 1.0 2020-8-20
 * @version 1.1 2020-8-28 新增根据日期字符串获取日期方法
 * @version 1.2 2020-9-25 新增获取当前系统日期字符串方法
 * @since 1.0 2020-8-20
 */
public class TimeUtils {
    /** 构造方法私有化，防止生成实例 */
    protected TimeUtils() {}

    /**
     * 获取当前系统日期字符串
     *
     * @return 当前系统时间字符串，格式为：yyyy-MM-dd
     * @since 1.2
     */
    public static String getNowTimeStr() {
        return formatTimeStr(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取时间字符串, 默认格式为yyyyMMddHHmmssZ<br />
     * 如，2020-09-07 12:00:00将被解析为20200907120000Z
     *
     * @param date 日期对象
     * @return 格式化后的时间字符串
     * @since 1.0
     * @see TimeUtils#formatTimeStr(Date, String)
     */
    public static String formatTimeStr(Date date) {
        return formatTimeStr(date, "yyyyMMddHHmmss") + "Z";
    }

    /**
     * 使用自定义格式, 获取时间字符串
     *
     * @param date 日期
     * @param pattern 格式化格式，例如yyyyMMddHHmmss
     * @return 格式化后的时间字符串，日期为null时返回空字符串
     * @since 1.0
     * @see SimpleDateFormat#format(Date, StringBuffer, FieldPosition)
     */
    public static String formatTimeStr(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }
        return "";
    }
    /**
     * 根据字符串表示的时间，获取日期对象，默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return 格式化后的时间字符串
     * @since 1.1
     * @see TimeUtils#parseDateByStr(String, String)
     */
    public static Date parseDateByStr(String dateStr) throws ParseException {
        return parseDateByStr(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据字符串表示的时间，获取日期对象
     *
     * @return 格式化后的时间字符串, 如果时间字符串为空或空字符串时返回null
     * @since 1.1
     * @see SimpleDateFormat#parse(String)
     */
    public static Date parseDateByStr(String dateStr, String pattern) throws ParseException {
        if (dateStr == null || dateStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(dateStr);
        }
        return null;
    }

    /**
     * 根据Date时间获取本年已过天数
     *
     * @return 返回本年已过天数
     * @since 1.1
     * @see
     */
    public static int getDaysPassedThisYear(Date date){
        return getCalendarBydate(date).get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 根据Date时间获取本月已过天数
     *
     * @return 返回本月已过天数
     * @since 1.1
     * @see
     */
    public static int getDaysPassedThisMonth(Date date){
        return getCalendarBydate(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据Date时间获取本周已过天数
     *
     * @return 返回本周已过天数
     * @since 1.1
     * @see
     */
    public static int getDaysPassedThisWeek(Date date){
        int i = getCalendarBydate(date).get(Calendar.DAY_OF_WEEK);
        if (i == 1){
            i = 7;
            return i;
        }else {
            return --i;
        }
    }

    /**
     * 根据Date时间获取日期类
     *
     * @return 返回日期类
     * @since 1.1
     * @see
     */
    private static Calendar getCalendarBydate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
