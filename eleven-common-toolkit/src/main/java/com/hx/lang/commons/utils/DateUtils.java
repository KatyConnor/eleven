package com.hx.lang.commons.utils;

import com.hx.lang.commons.enums.DedlnTypeEnum;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 * <p>
 * 各种常用的日期工具静态函数
 *
 * @author wml
 */
public class DateUtils {

    public static final String SDF_14 = "yyyyMMddHHmmss";
    public static final String SDF_8 = "yyyyMMdd";
    public static final String SDF_MS_9 = "HHmmssSSS";
    public static final String SDF_F_8 = "yyyy-MM-dd";
    public static final String SDF_F_8_1 = "yyyy/MM/dd";
    public static final String SDF_F_8_2 = "yyyy年MM月dd日";
    public static final String SDF_F_14 = "yyyy-MM-ddHH:mm:ss";
    public static final String SDF_F_14_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String SDF_F_14_2 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String SDF_F_14_3 = "yyyyMMdd:HHmmss";
    public static final String SDF_F_MD_14_3 = "MMddHHmmssSSS";
    public static final String SDF_17 = "yyyyMMddHHmmssSSS";
    public static final String SDF_YYYY = "YYYY";
    public static final String SDF_MM = "MM";
    public static final String SDF_DD = "dd";
    public static final String SDF_F_6 = "HH:mm:ss";
    public static final String SDF_6 = "HHmmss";
    public static final String SDF_F_MMDD = "MM-dd";


    /**
     * 获取年月日
     */
    public static String getNowTime() {
        return new SimpleDateFormat(SDF_8).format(new Date());
    }

    /**
     * 取当前时间戳(迁移自CommonUtil)
     *
     * @return datetime 字符串类型时间戳，格式YYYY-MM-DD HH:MM:SS
     */
    public static String getTimeStampAsString() {
        return new SimpleDateFormat(SDF_F_14_1).format(new Date());
    }

    /**
     * 取当前时间戳(迁移自CommonUtil)
     *
     * @return datetime Timestamp类型时间戳，格式YYYY-MM-DD HH:MM:SS
     */
    public static Timestamp getTimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat(SDF_F_14_1);
        return Timestamp.valueOf(df.format(new Date()));
    }

    /**
     * 将时间转换为固定格式的时间戳
     *
     * @param df
     * @param date
     * @return
     */
    public static Timestamp date2TimeStamp(String df, Date date) {
        if (StringUtils.isBlank(df)) {
            df = SDF_F_14_1;
        }
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(df);
        return Timestamp.valueOf(dateFormat.format(date));
    }

    /**
     * 判断是否为闰年
     * <p>
     * 判断方法为：1、非整百年：能被4整除的为闰年
     * 2、整百年：能被400整除的是闰年
     * 3、如果能被3200整除，并且能被172800整除才是闰年
     *
     * @return boolean true 是闰年; false 非闰年
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        } else if (year % 400 == 0) {
            if (year % 3200 == 0 && year % 172800 != 0)
                return false;
            return true;
        } else
            return false;
    }

    /**
     * 将时间转换为特定格式字符串
     *
     * @param date
     * @param intStyle
     * @return
     */
    public static String getDateToStr(Date date, int intStyle) {
        SimpleDateFormat sdf = null;
        switch (intStyle) {
            case 1:
                sdf = new SimpleDateFormat(SDF_F_14_1);
                break;
            case 2:
                sdf = new SimpleDateFormat(SDF_F_14_2);
                break;
            case 3:
                sdf = new SimpleDateFormat(SDF_F_8_1);
                break;
            case 4:
                sdf = new SimpleDateFormat(SDF_F_8_2);
                break;
            case 5:
                sdf = new SimpleDateFormat(SDF_8);
                break;
            case 6:
                sdf = new SimpleDateFormat(SDF_MS_9);
                break;
            case 7:
                sdf = new SimpleDateFormat(SDF_F_14_3);
                break;
            case 8:
                sdf = new SimpleDateFormat(SDF_F_MD_14_3);
                break;
            case 9:
                sdf = new SimpleDateFormat(SDF_14);
                break;
            case 10:
                sdf = new SimpleDateFormat(SDF_17);
                break;
            case 11:
                sdf = new SimpleDateFormat(SDF_DD);
                break;
            case 12:
                sdf = new SimpleDateFormat(SDF_F_6);
                break;
            case 13:
                sdf = new SimpleDateFormat(SDF_6);
                break;
            case 14:
                sdf = new SimpleDateFormat(SDF_F_MMDD);
                break;
            default:
                sdf = new SimpleDateFormat(SDF_F_8);
                break;
        }
        return sdf.format(date).toString();
    }


    /**
     * 将时间转换为特定格式字符串
     *
     * @param date
     * @param intStyle
     * @return
     */
    public static Date formatDate(String date, int intStyle) {
        SimpleDateFormat sdf = null;
        switch (intStyle) {
            case 1:
                sdf = new SimpleDateFormat(SDF_F_14_1);
                break;
            case 2:
                sdf = new SimpleDateFormat(SDF_F_14_2);
                break;
            case 3:
                sdf = new SimpleDateFormat(SDF_F_8_1);
                break;
            case 4:
                sdf = new SimpleDateFormat(SDF_F_8_2);
                break;
            case 5:
                sdf = new SimpleDateFormat(SDF_8);
                break;
            case 6:
                sdf = new SimpleDateFormat(SDF_MS_9);
                break;
            case 7:
                sdf = new SimpleDateFormat(SDF_F_14_3);
                break;
            case 8:
                sdf = new SimpleDateFormat(SDF_F_MD_14_3);
                break;
            case 9:
                sdf = new SimpleDateFormat(SDF_14);
                break;
            case 10:
                sdf = new SimpleDateFormat(SDF_17);
                break;
            case 11:
                sdf = new SimpleDateFormat(SDF_DD);
                break;
            case 12:
                sdf = new SimpleDateFormat(SDF_F_6);
                break;
            case 13:
                sdf = new SimpleDateFormat(SDF_6);
                break;
            case 14:
                sdf = new SimpleDateFormat(SDF_F_MMDD);
                break;
            default:
                sdf = new SimpleDateFormat(SDF_F_8);
                break;
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加天数
     *
     * @param date
     * @param n
     * @return
     */
    public static String ADD_DAY(String date, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SDF_F_8);
            date = sdf.format(getDate(date));
            return ADD_DATE(Calendar.DATE, date, n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String ADD_DAY(String date, int n, SimpleDateFormat dateFormat) {
        try {
            date = dateFormat.format(getDate(date));
            return ADD_DATE(Calendar.DATE, date, n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加天数
     *
     * @param date
     * @param n
     * @return
     */
    public static String ADD_DAY(Date date, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat(SDF_F_8);
        String dateStr = sdf.format(date);
        return ADD_DATE(Calendar.DATE, dateStr, n);
    }

    /**
     * 获得指定日期在当周是第几天
     *
     * @param dateTime
     * @return
     */
    public static int getDayOfWeek(Date dateTime) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前时间所在月的最大天数
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static int getMaxMonthDay(Date dateTime) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得指定日期在当月是第几周
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static int getWeekOfMonth(Date dateTime) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得指定日期在当年是第几周
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static int getWeekOfYear(Date dateTime) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 日期相减获取天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static double getSubDays(String startDate, String endDate) throws ParseException {
        SimpleDateFormat startFormat = new SimpleDateFormat(SDF_F_8);
        SimpleDateFormat endFormat = new SimpleDateFormat(SDF_F_8);
        long dateTime = endFormat.parse(endDate).getTime() - startFormat.parse(startDate).getTime();
        return dateTime / 86400000;

    }

    /**
     * 日期相减获取月数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static double getSubtractDays(String startDate, String endDate) throws ParseException {
        SimpleDateFormat startFormat = new SimpleDateFormat(SDF_8);
        SimpleDateFormat endFormat = new SimpleDateFormat(SDF_8);
        long dateTime = endFormat.parse(endDate).getTime() - startFormat.parse(startDate).getTime();
        return dateTime / 86400000;

    }

    /**
     * 获取8位日期
     *
     * @param date
     * @return
     */
    public static String getDate8(String date) {
        if (date != null && date.length() > 8) {
            date = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        }
        return date;
    }

    /**
     * 取两个时间的月数
     *
     * @param startDate yyyyMMdd
     * @param endDate
     * @return
     */
    public static int countMonth(String startDate, String endDate) {
        int rv = 0;

        if (Long.parseLong(startDate) > Long.parseLong(endDate)) {
            String tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        int yy1 = Integer.parseInt(startDate.substring(0, 4));
        int mm1 = Integer.parseInt(startDate.substring(4, 6));
        int dd1 = Integer.parseInt(startDate.substring(6));

        int yy2 = Integer.parseInt(endDate.substring(0, 4));
        int mm2 = Integer.parseInt(endDate.substring(4, 6));
        int dd2 = Integer.parseInt(endDate.substring(6));

        rv = (yy2 - yy1) * 12 + (mm2 - mm1);

        if (dd2 >= dd1) {
            rv = rv + 1;
        }
        return rv;
    }

    /**
     * 获取指定年前的年日期
     * 返回格式：yyyy
     *
     * @param amount
     */
    public static String getYearBack(int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -amount);
        return new SimpleDateFormat("yyyy").format(c.getTime());

    }


    /**
     * 增加月数
     *
     * @param date
     * @param n
     * @return
     */
    public static String ADD_MONTH(String date, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SDF_F_8);
            date = sdf.format(getDate(date));
            return ADD_DATE(Calendar.MONTH, date, n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getDate(String format) {
        String[] str = new String[]{"yyyy-MM", "yyyyMM", "yyyy-MM-dd", "yyyyMMdd", "yyyy", "yyyy-MM-dd HH:mm:ss"};
        SimpleDateFormat simpleDateFormat;
        Date date = null;
        for (String param : str) {
            if (format.length() != param.length()) {
                continue;
            }
            simpleDateFormat = new SimpleDateFormat(param);
            try {
                date = simpleDateFormat.parse(format);
                return date;
            } catch (Exception e) {
                continue;
            }
        }
        if (date == null) {
            simpleDateFormat = new SimpleDateFormat(SDF_F_8);
            try {
                date = simpleDateFormat.parse(format);
                return date;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String ADD_DATE(int optype, String date, int num) {
        String st_return = "";
        try {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(optype, num);
            if (optype == Calendar.MONTH) {
                calendar.add(Calendar.DATE, -1);
            }
            String st_m = "";
            String st_d = "";
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            if (m <= 9) {
                st_m = "0" + m;
            } else {
                st_m = "" + m;
            }
            if (d <= 9) {
                st_d = "0" + d;
            } else {
                st_d = "" + d;
            }
            st_return = y + "-" + st_m + "-" + st_d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return st_return;
    }

    /**
     * 日期相加
     *
     * @param date 旧的时间
     * @param day  增加量
     * @return
     * @throws ParseException
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time = day + time; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * 年月日日期相加
     *
     * @param type
     * @param num
     */
    public static String addDateRel(String type, int num) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起始时间
        if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_000.getCode(), type)) {//日
            cal.add(Calendar.DATE, num);
        } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_001.getCode(), type)) {//月
            cal.add(Calendar.MONTH, num);
        } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_002.getCode(), type)) {//年
            cal.add(Calendar.YEAR, num);
        }
        return getDateToStr(cal.getTime(), 5);
    }

    /**
     * 年月日日期相加
     *
     * @param date yyyyMMdd 格式字符串
     * @param type
     * @param num
     */
    public static String addDateRel(String date, String type, int num) {
        String dateToStr = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date dates = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);//设置起始时间
            if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_000.getCode(), type)) {//日
                cal.add(Calendar.DATE, num);
            } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_001.getCode(), type)) {//月
                cal.add(Calendar.MONTH, num);
            } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_002.getCode(), type)) {//年
                cal.add(Calendar.YEAR, num);
            }
            dateToStr = getDateToStr(cal.getTime(), 5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToStr;
    }

    /**
     * 获取营业日期
     *
     * @return
     */
    public static String openDay() {
        /*try {
            return BusinessDateUtils.openDay(null);
        } catch (QueryQuotaException e) {
            throw new RuntimeException(e.getChineseMessage());
        }*/

        try {
            // 系统日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取上一营业日期
     *
     * @return
     * @throws QueryQuotaException
     */
//    public static String lastOpenDay() {
//        try {
//            return BusinessDateUtils.lastOpenDay(null);
//        } catch (QueryQuotaException e) {
//            throw new RuntimeException(e.getChineseMessage());
//        }
//    }

    /**
     * 根据生日计算周岁
     *
     * @param bornDate 生日
     * @return 周岁
     * @throws ParseException
     */
    public static int getFullYear(String bornDate) throws ParseException {
        if (bornDate.length() == 8) {
            try {
                SimpleDateFormat sdf8 = new SimpleDateFormat(SDF_8);
                SimpleDateFormat sdf10 = new SimpleDateFormat(SDF_F_8);
                Date date8 = sdf8.parse(bornDate);
                bornDate = sdf10.format(date8);
            } catch (ParseException e) {
                throw new RuntimeException("时间解析异常：", e);
            }
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(SDF_F_8);
        Date date = sdf.parse(getTimeStampAsString());//获取当前时间
        calendar.setTime(date);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DATE);
        Date birthday = sdf.parse(bornDate);
        calendar.setTime(birthday);
        int bornYear = calendar.get(Calendar.YEAR);
        int bornMonth = calendar.get(Calendar.MONTH);
        int bornDay = calendar.get(Calendar.DATE);
        int age = nowYear - bornYear;
        if (nowMonth > bornMonth || (nowMonth == bornMonth && nowDay >= bornDay)) {
            age++;
        }
        return age;
    }

    /**
     * 两个时间相比，相等为0，大于为1，小于为-1
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static int compareDate(String date1, String date2) throws Exception {
        SimpleDateFormat sdf8 = new SimpleDateFormat(SDF_8);
        SimpleDateFormat sdf10 = new SimpleDateFormat(SDF_F_8);
        if (date1.length() == 8) {
            Date d1 = sdf8.parse(date1);
            date1 = sdf10.format(d1);
        }
        if (date2.length() == 8) {
            Date d2 = sdf8.parse(date2);
            date2 = sdf10.format(d2);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date1).compareTo(sdf10.parse(date2));
    }

    /**
     * 两个时间相比，相等为0，大于为1，小于为-1
     *
     * @param date1 yyyymmdd
     * @param date2 yyyymmdd
     * @return
     * @throws Exception
     */
    public static int compareDate2(String date1, String date2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(date1).compareTo(sdf.parse(date2));
    }

    public final static String DTPattern = "yyyy-MM-dd  HH:mm:ss ";

    public static String getDateTime(String pattern) {
        String dateTime = "";
        Calendar calender = Calendar.getInstance();
        if (pattern == null || pattern.equals(""))
            pattern = DTPattern;
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        dateTime = sf.format(calender.getTime());
        return dateTime;
    }

    /**
     * 获取当前时间的前 N 天的时间字符串
     *
     * @return
     */
    public static String getLastDateStr(int number, String df) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - number);
        if (StringUtils.isBlank(df)) {
            df = "yyyy-MM-dd";
        }
        SimpleDateFormat sf = new SimpleDateFormat(df);
        String format = sf.format(now.getTime());
        return format;
    }

    /**
     * 获取当前时间的前 N 月天的时间字符串
     *
     * @return
     */
    public static String getLastMonthDateStr(int number, String df) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MONTH, -number);
        if (StringUtils.isBlank(df)) {
            df = "yyyy-MM-dd";
        }
        SimpleDateFormat sf = new SimpleDateFormat(df);
        String format = sf.format(now.getTime());
        return format;
    }

    /**
     * 时间8位转10位
     *
     * @param dateString
     * @return
     */
    public static String date8To10(String dateString) {
        try {
            if (StringUtils.isBlank(dateString)) {
                return null;
            }
            Date date = getDate(dateString);
            SimpleDateFormat sdf10 = new SimpleDateFormat(SDF_F_8);
            return sdf10.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 时间10位转8位
     *
     * @param dateString
     * @return
     */
    public static String date10To8(String dateString) {
        try {
            if (StringUtils.isBlank(dateString)) {
                return null;
            }
            Date date = getDate(dateString);
            SimpleDateFormat sdf8 = new SimpleDateFormat(SDF_8);
            return sdf8.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param dateString 日期八位 如：20191025
     * @param num        增加 或减少的月份   如  1（加一个月）   -1（减一个月）
     * @return 加减月份后的日期  如  20191125
     */
    public static String monthDateAdd(String dateString, int num) {
        try {
            if (StringUtils.isBlank(dateString)) {
                return null;
            }
            SimpleDateFormat startFormat = new SimpleDateFormat("yyyyMMdd");
            Date parse = startFormat.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(parse);
            cal.add(Calendar.MONTH, num);
            Date time = cal.getTime();
            String format = startFormat.format(time);

            return format;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 返回贷款止期
     *
     * @param type
     * @param num
     */
    public static String getDueDate(String type, int num) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起始时间
        if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_000.getCode(), type)) {//日
            cal.add(Calendar.DATE, num);
        } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_001.getCode(), type)) {//月
            cal.add(Calendar.MONTH, num);
        } else if (StringUtils.equals(DedlnTypeEnum.DEDLN_TYPE_002.getCode(), type)) {//年
            cal.add(Calendar.YEAR, num);
        }
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        return getDateToStr(cal.getTime(), 5);
    }

    /**
     * 获取短信的时间戳
     *
     * @return
     */
    public static String formatNewDate() {
        StringBuilder data = new StringBuilder();
        String tempDate = new SimpleDateFormat(SDF_F_14).format(new Date());
        data.append(tempDate.substring(0, 10)).append("T").append(tempDate.substring(10, 18)).append(".000+08:00");
        return data.toString();
    }

    /**
     * 获取当日开始时间
     *
     * @return
     */
    public static String nowDateStart() {
        String startDate = new SimpleDateFormat(SDF_F_8).format(new Date());
        SimpleDateFormat sf = new SimpleDateFormat(SDF_F_14_1);
        try {
            return sf.format(sf.parse(startDate + " 00:00:00"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当日结束时间
     *
     * @return
     */
    public static String nowDateEnd() {
        String startDate = new SimpleDateFormat(SDF_F_8).format(new Date());
        SimpleDateFormat sf = new SimpleDateFormat(SDF_F_14_1);
        try {
            return sf.format(sf.parse(startDate + " 24:59:59"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String res = getLastDateStr(29, SDF_F_8);
        Date da = addDate(new Date(), -29);
        String adDay = ADD_DAY("2023-09-20", -29);
        System.out.println(res);
        System.out.println(new SimpleDateFormat(SDF_F_8).format(da));
        System.out.println(adDay);
    }
}
