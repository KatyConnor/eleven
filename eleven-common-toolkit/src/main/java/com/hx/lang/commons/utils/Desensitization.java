package com.hx.lang.commons.utils;

/**
 * @ClassName:  Desensitization   
 * @Description:脱敏工具类
 * @author: yangming 
 * @date:   2018年9月19日 下午8:43:35   
 *     
 * 
 */
public class Desensitization {
	
	/**   
	 * @Title: maskingName   
	 * @Description: 对企业姓名和个人姓名进行脱敏
	 * @param: @param name
	 * @param: @return      
	 * @return: String      
	 * @throws   
	 */
	public static String maskingName(String name) {
		String replaceStr = "";
		if (StringUtils.isBlank(name)) {
			return "";
		}
		if (name.length() <= 2) {
			return name.substring(0, 1) + "*";
		} else {
			for (int k = 0; k < name.length() - 2; k++) {
				replaceStr += "*";
			}
			// 截取第一位和最后一位拼接
			return name.substring(0, 1) + replaceStr
					+ name.substring(name.length() - 1, name.length());
		}
    }
	
	/**   
	 * @Title: maskingNo   
	 * @Description: 对企业编号和个人证件编号进行脱敏  
	 * @param: @param idNo
	 * @param: @return      
	 * @return: String      
	 * @throws   
	 */
	public static String maskingNo(String idNo) {
		String replaceStr = "";
		if (StringUtils.isBlank(idNo)) {
			return "";
		}
		idNo =StringUtils.trim(idNo);
		if (idNo.length() <= 10) {
			for (int i = 0; i < idNo.length() - 4; i++) {
				replaceStr += "*";
			}
			return idNo.substring(0, 4) + replaceStr;
		} else {

			for (int i = 0; i < idNo.length() - 10; i++) {
				replaceStr += "*";
			}
			return idNo.substring(0, 5) + replaceStr
					+ idNo.substring(idNo.length() - 5, idNo.length());
		}
    }
} 
