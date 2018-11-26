package com.example.administrator.txt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;

/**
 * <pre>
 *     author : Nian
 *     e-mail : 457505316@qq.com
 *     time  : 2017-12-30
 *     desc  :
 *     version: 1.0
 * </pre>
 */
public class DateUtils {
    private static final int YEAR = 365 * 24 * 60 * 60;// 年

    private static final int MONTH = 30 * 24 * 60 * 60;// 月

    private static final int DAY = 24 * 60 * 60;// 天

    private static final int HOUR = 60 * 60;// 小时

    private static final int MINUTE = 60;// 分钟

    public static void main(String[] args) {
        String time = "2010年12月08日11时17分00秒";
        System.out.println(time);
        // 字符串=======>时间戳
        String re_str = getTime(time);
        System.out.println(re_str);
        // 时间戳======>字符串  String data = getStrTime(re_str);
        System.out.println(data);
    }

    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }


    // 将时间戳转为字符串  用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
    // 将时间戳转为字符串  用此方法输入所要转换的时间戳输入例如（1402733340000）输出（"2014年06月14日16时09分00秒"）
    public static String getStrTime1(String cc_time) {
        String re_StrTime = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));
        return re_StrTime;
    }


    /**
     * 解析时间
     * "yyyyMMddHHmmss" to "yyyy年MM月dd日"
     * @param strTime
     * @return
     */
    public static String getCorrectDate(String strTime) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    /**
     * 解析时间
     * "yyyyMMddHHmmss" to "yyyy年MM月dd日"
     * @param strTime
     * @return
     */
    public static String getCorrectDate1(String strTime) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 解析时间
     * "yyyy-MM-dd HH:mm:ss" to "HH:mm"
     * @param strTime
     * @return
     */
    public static String getFaultTime(String strTime) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * 解析时间
     * "yyyyMMddHHmmss" to "yyyy-MM-dd HH:mm"
     * @param strTime
     * @return
     */
    public static String getCorrectDateTime(String strTime) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    /**
     * 解析时间
     * "yyyyMMddHHmmss" to "HH:mm"
     * @param strTime
     * @return
     */
    public static String getCorrectTime(String strTime) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * 获取当前时间，输出格式
     * "yyyyMMddHHmmss"
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    /**
     * 获取当前时间，输出格式
     * "yyyy年MM月dd日HH时mm分ss秒"
     */
    public static String getCurrentTime1() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return formatter.format(new Date());
    }

    /**
     * 获取当前时间，输出格式
     * "yyyy年MM月dd日HH时mm分ss秒"
     */
    public static String getCurrentTime2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(new Date());
    }
    /**
     * 获取当前时间，输出格式
     * "yyyy年MM月dd日"
     */
    public static String getCurrentTime3() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }    /**
     * 获取当前时间，输出格式
     * "yyyy年MM月dd日HH时mm分ss秒"
     */
    public static String getCurrentTime4() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(new Date());
    }

    /**
     * 获取未来时间距今相隔的天数
     */
    public static int getDayCount(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long temp = null;
        try {
            temp = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        temp -= System.currentTimeMillis();
        int days = (int) (temp / 1000 / 3600 / 24);
        return days;
    }

    /**
     * 获取当前日期的指定格式的字符串
     */

    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern("yyyy-MM-dd");
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * string类型转换为date类型
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    public static Date stringToDate(String strTime, String formatType) {

        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * string类型转换为long类型
     */
    public static long stringToLong(String strTime) {

        // String类型转成date类型
        Date date = stringToDate(strTime, "yyyy-MM-dd");
        if (date == null) {
            return 0;
        } else {
            // date类型转成long类型
            return date.getTime();
        }
    }

    /**
     * long类型转换为String类型
     * currentTime要转换的long类型的时间
     */

    public static String longToString(long currentTime) {

        String strTime;
        // long类型转成Date类型
        Date date = new Date(currentTime);
        // date类型转成String
        strTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return strTime;
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(String time) {

        long timestamp = stringToLong(time);
        long currentTime = System.currentTimeMillis();
        // 与现在时间相差秒数
        long timeGap = (currentTime - timestamp) / 1000;
        System.out.println("timeGap: " + timeGap);
        String timeStr;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /***
     * 获取当前日期距离过期时间的日期差值
     * @param endTime
     * @return
     */

    public static String dateDiff(String endTime) {
        String strTime = null;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sd.format(curDate);
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(str).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            if (day >= 1) {
                strTime = day + "天" + hour + "时";
            } else {

                if (hour >= 1) {
                    strTime = day + "天" + hour + "时" + min + "分";

                } else {
                    if (sec >= 1) {
                        strTime = day + "天" + hour + "时" + min + "分" + sec + "秒";
                    } else {
                        strTime = "显示即将到期";
                    }
                }
            }

            return strTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }
}

