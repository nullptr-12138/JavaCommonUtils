package com.nullptr.utils.system;

import org.apache.commons.lang3.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 日期工具类
 *
 * @author nullptr
 * @version 1.0 2020-8-20
 * @version 1.1 2020-8-28 新增根据日期字符串获取日期方法
 * @version 1.2 2020-9-25 新增获取当前系统日期字符串方法
 * @version 1.3 2020-9-10-31 继承org.apache.commons.lang3.time.DateUtils，精简部分已存在方法
 * @since 1.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 构造方法私有化，防止生成实例 */
    private DateUtils() {}

    /**
     * 获取当前系统日期字符串
     *
     * @return 当前系统时间字符串，格式为：yyyy-MM-dd
     * @since 1.2
     */
    public static String getNowTimeStr() {
        return formatDate(null, DATE_FORMAT);
    }

    /**
     * 获取当前系统日期字符串
     *
     * @return 当前系统时间字符串，格式为：yyyy-MM-dd-HH:mm:ss
     * @since 1.2
     */
    public static String getNowDateTimeStr() {
        return formatDate(null, DATETIME_FORMAT);
    }

    /**
     * 使用自定义格式, 获取时间字符串
     *
     * @param date 日期，为空则使用当前日期
     * @return 格式化后的时间字符串
     * @since 1.0
     */
    public static String formatDate(Date date, String pattern) {
        date = ObjectUtils.defaultIfNull(date, new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 日期转换为毫秒数
     *
     * @param date 日期
     * @return 当前日期总毫秒数
     * @since 1.0
     */
    public static long toMilliseconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 毫秒数转换为日期
     *
     * @param milliseconds 毫秒数
     * @return 当前毫秒数所表示的日期
     * @since 1.0
     */
    public static Date fromMillisecond(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.getTime();
    }
}
