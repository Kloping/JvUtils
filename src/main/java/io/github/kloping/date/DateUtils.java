package io.github.kloping.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author github-kloping
 */
public class DateUtils {
    private static final SimpleDateFormat SF_HH = new SimpleDateFormat("HH");
    private static final SimpleDateFormat SF_DD = new SimpleDateFormat("dd");
    private static final SimpleDateFormat SF_MM = new SimpleDateFormat("MM");
    private static final SimpleDateFormat SF_YY = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat SF_0 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

    /**
     * 返回小时
     *
     * @return
     */
    public static Integer getHour() {
        return Integer.parseInt(SF_HH.format(new Date()));
    }

    /**
     * 返回日
     *
     * @return int
     */
    public static int getDay() {
        return Integer.parseInt(SF_DD.format(new Date()));
    }

    /**
     * 返回月
     *
     * @return int
     */
    public static int getMonth() {
        return Integer.parseInt(SF_MM.format(new Date()));
    }

    /**
     * 返回年
     *
     * @return int
     */
    public static int getYear() {
        return Integer.parseInt(SF_YY.format(new Date()));
    }

    /**
     * 返回格式 时间 例 2021-10-29:15:45:09
     *
     * @return 2021-10-29:15:45:09
     */
    public static String getFormat() {
        return SF_0.format(new Date());
    }

    /**
     * 获取 从 当前时间到 未来指定时间 的 毫秒值 例:
     * 2021-10-29:15:45:09
     *
     * @param format yyyy-MM-dd:HH:mm:ss
     * @return long
     */
    public static long getTimeStamp(String format) {
        try {
            return SF_0.parse(format).getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static final String[] WEEK_DAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEK_DAYS[w];
    }

    /**
     * 获取距现还有多长分钟
     *
     * @param time
     * @return
     */
    public static String getTimeTipsMinutes(long time) {
        long v = time - System.currentTimeMillis();
        if (v >= 60000L) {
            return (v / 60000L) + "分钟";
        } else {
            return (v / 1000) + "秒";
        }
    }

    /**
     * 获取距现还有多长天
     *
     * @param time
     * @return
     */
    public static String getTimeTipsDays(long time) {
        long v = time - System.currentTimeMillis();
        if (v >= 86400000L) {
            return (v / 86400000L) + "天";
        } else {
            return (v / 3600000) + "时";
        }
    }

    /**
     * 获取当前是今年的第几周
     *
     * @return
     */
    public static Integer getWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        return weekOfYear;
    }
}
