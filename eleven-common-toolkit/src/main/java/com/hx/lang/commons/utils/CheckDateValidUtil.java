package com.hx.lang.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期格式检查工具类
 */
public class CheckDateValidUtil {

    /**
     *检验日期格式为 ： yyyy
     * @param date  (yyyy)
     * @return
     */
    public static boolean isValidDate(String date){
        boolean converSucess = false;

        if(null == date || date.isEmpty()){
            return converSucess;
        }
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy");
        try {
            SimpleDateFormat.parse(date);
            converSucess = true;
        } catch (ParseException e) {
            //若是出现异常，则日期格式不对
            converSucess = false;
        }
        return converSucess;
    }


    /**
     *检验日期格式为 ： yyyyMMdd
     * @param date  (yyyyMMdd)
     * @return
     */
    public static boolean isValidDate2(String date){
        boolean converSucess = false;

        if(null == date || date.isEmpty()){
            return converSucess;
        }
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            SimpleDateFormat.parse(date);
            converSucess = true;
        } catch (ParseException e) {
            //若是出现异常，则日期格式不对
            converSucess = false;
        }
        return converSucess;
    }

    /**
     *检验日期格式为 ： yyyy/MM/dd
     * @param date  (yyyy/MM/dd)
     * @return
     */
    public static boolean isValidDate3(String date){
        boolean converSucess = false;

        if(null == date || date.isEmpty()){
            return converSucess;
        }
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            SimpleDateFormat.parse(date);
            converSucess = true;
        } catch (ParseException e) {
            //若是出现异常，则日期格式不对
            converSucess = false;
        }
        return converSucess;
    }
}
