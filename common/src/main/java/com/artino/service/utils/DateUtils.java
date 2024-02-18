package com.artino.service.utils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Objects;

public class DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy/MM";

    public static String YYYY_MM_DD = "yyyy/MM/dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final long timeSpan() {
        return getNowDate().getTime();
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, getNowDate());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(String format, final Date date)
    {
        if (Objects.isNull(format)) format = YYYY_MM_DD_HH_MM_SS;
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(getNowDate());
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(getNowDate());
    }

    public static Date parseDate(String str) {
        return parseDate(str, null);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(String str, String fmt)
    {
        if (Objects.isNull(str)) return null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(Objects.isNull(fmt) ? YYYY_MM_DD_HH_MM_SS : fmt);
            return sdf.parse(str);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差（单位：分钟）
     *
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        if (Objects.isNull(startTime)) startTime = getNowDate();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒
        long sec = diff % nd % nh % nm / ns;

        StringBuilder sb = new StringBuilder();
        if (day > 0) sb.append(day + "天");
        if (day > 0 || hour > 0) sb.append(day + "时");
        if (day > 0 || hour > 0 || min > 0) sb.append(min + "分");
        sb.append(sec + "秒");
        return sb.toString();
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取多少分钟之后
     * @param miniuts
     * @return
     */
    public static Date after(int miniuts) {
        long currentTime = System.currentTimeMillis();
        currentTime += (long) miniuts * 60 * 1000;
        return new Date(currentTime);
    }

    public static Date after(String time, int miniuts) {
        long ts = parseDate(time).getTime();
        return new Date(ts + (long) miniuts * 60 * 1000);
    }

    public static long timespan(Date date) {
        if (Objects.isNull(date)) date = getNowDate();
        return date.getTime();
    }
}