package io.github.kloping.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author github-kloping
 */
public class DateUtils {
    private static final SimpleDateFormat SF_DD = new SimpleDateFormat("dd");
    private static final SimpleDateFormat SF_MM = new SimpleDateFormat("MM");
    private static final SimpleDateFormat SF_YY = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat SF_0 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

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
}
