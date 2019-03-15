package cn.feihutv.zhibofeihu.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * 时间工具类
 *
 * @author way
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {


    /**
     * 一些时间格式
     */
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
    public final static String FORMAT_DATE = "yyyy-MM-dd";


    /**
     * 类似QQ/微信 聊天消息的时间
     */
    public static String getChatTime(boolean hasYear, long timesamp) {
        long clearTime = timesamp;
        String result;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;
            default:
                result = getTime(hasYear, clearTime);
                break;
        }
        return result;
    }

    private static String getTime(boolean hasYear, long time) {
        String pattern = FORMAT_DATE_TIME;
        if (!hasYear) {
            pattern = FORMAT_MONTH_DAY_TIME;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    private static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
        return format.format(new Date(time));
    }


    /**
     * 秒
     *
     * @param time
     * @return
     */
    public static String converFeedTime(long time) {
        //long currentSeconds = System.currentTimeMillis();
        Date dt = new Date();
        long currentSeconds = dt.getTime();
        long timeGap = currentSeconds / 1000 - time;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 3 * 30 * 24 * 60 * 60) {// 3个月以上就返回标准时间
            timeStr = getDayTime(time * 1000) + " " + getMinTime(time * 1000);
        } else if (timeGap > 30 * 24 * 60 * 60) {// 1个月以上
            int mo = (int) timeGap / (30 * 24 * 60 * 60);
            timeStr = mo + "个月前";
        } else if (timeGap > 24 * 60 * 60) {// 1天以上
            int day = (int) timeGap / (24 * 60 * 60);
            timeStr = day + "天前";
        } else if (timeGap > 60 * 60) {// 1分钟-59分钟
            int hour = (int) timeGap / (60 * 60);
            timeStr = hour + "小时前";
        } else if (timeGap > 60) {// 1秒钟-59秒钟
            int min = (int) timeGap / (60);
            timeStr = min + "分钟前";
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }


    public static String getDayTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE);
        return format.format(new Date(time));
    }

    public static String getDay(long time) {
        String day = "";
        time = time * 1000;
        SimpleDateFormat format = new SimpleDateFormat("dd");
        day = format.format(new Date(time));
        try {
            day = Integer.parseInt(day) + "";
        } catch (NumberFormatException e) {
        }
        return day;
    }

    public static String getMonth(long time) {
        String month = "";
        time = time * 1000;
        SimpleDateFormat format = new SimpleDateFormat("MM");
        month = format.format(new Date(time));
        try {
            month = Integer.parseInt(month) + "";
        } catch (NumberFormatException e) {
        }
        return month;
    }

    public static String getMVTime(long time) {
        String month = "";
        time = time * 1000;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        month = format.format(new Date(time));
        try {
            month = Integer.parseInt(month) + "";
        } catch (NumberFormatException e) {
        }
        return month;
    }

    public static String getMVTimeYYmmdd(long time) {
        String month = "";
        time = time * 1000;
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_TIME);
        month = format.format(new Date(time));
        try {
            month = Integer.parseInt(month) + "";
        } catch (NumberFormatException e) {
        }
        return month;
    }

    public static String getMonthTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(time));
    }

    public static String getMinTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
        return format.format(new Date(time));
    }

    public static String getAllTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_TIME_SECOND);
        return format.format(new Date(time));
    }

    //1月1日
    //上午 8:20
    public static String getMdHmTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("M月d日 H h:mm");
        return format.format(new Date(time));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return parseInt(String.valueOf(between_days));
    }
}
