package com.hx.nine.eleven.jobtask.utils;

import com.hx.nine.eleven.logchain.toolkit.util.HXLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wml
 * @Discription
 * @Date 2023-09-25
 */
public class DateUtil {

    public static Date parseStrToDate(String date,String format){
        SimpleDateFormat sdf =  new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            HXLogger.build(DateUtil.class).error("时间格式化转换失败！",e);
        }
        return null;
    }
}
