package cn.gmw.api.meiyou.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtil {

    public static String getFormatdate(Long l) {
        String s = "";
        if (l != null) {
            Date d = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
            s = dateFormat.format(d);
        }
        return s;
    }

    public static long getTimemills(String daytime) {
        long ret = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ret = sdf.parse(daytime).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public static String getWhatDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        GregorianCalendar gc = new GregorianCalendar();
//        gc.set(Calendar.YEAR, 2018);//设置年
//        gc.set(Calendar.MONTH, 5);//这里0是1月..以此向后推
//        gc.set(Calendar.DAY_OF_MONTH, 20);//设置天
//        date = gc.getTime();
        return sdf.format(date);
    }

    /**
     * 根据日期和格式进行日期格式化
     *
     * @date 2015年12月14日 上午11:09:54
     * @author 0-Vector
     */
    public static String format(Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String date = sdf.format(d);
        return date;
    }

    /**
     * 根据日期(long)和格式进行日期格式化
     *
     * @date 2015年12月14日 上午11:13:26
     * @author 0-Vector
     */
    public static String format(long d, String pattern) {
        Date date = new Date(d);
        return format(date, pattern);
    }

    /**
     * 获取今日零点的毫秒数
     *
     * @date 2016年2月19日 下午6:24:49
     * @author 0-Vector
     */
    public static long getToday0OclockMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}