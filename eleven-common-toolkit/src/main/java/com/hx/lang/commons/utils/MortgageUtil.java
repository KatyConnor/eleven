package com.hx.lang.commons.utils;


import java.util.HashMap;
import java.util.UUID;


/**
 * 主键生成工具类
 * @author 
 * */
public class MortgageUtil {
	

	public static HashMap<String, String> dealPage(int currentPage,int pageSize,int totalSize){
		
		HashMap<String, String> result = new HashMap<String, String>();
		if(currentPage <=1 ) {
			result.put("StartNum", "0");
			result.put("EndNum", String.valueOf(pageSize));
		}else {
			result.put("StartNum", String.valueOf((currentPage-1)*pageSize));
			if(currentPage*pageSize >totalSize ) {
				result.put("EndNum", String.valueOf(totalSize));
			}
			result.put("EndNum", String.valueOf(currentPage*pageSize));
		}
		return result;
		
	}
	

	
	/**
	 * 生成随机编号
	 * @param type 随机编号类型
	 * */

	public static String randomNo(String type) {
		int i = (int)(Math.random()*8999999)+1000000;
		return type+i;
	}
	/**
	 * 生成随机编号
	 *
	 **/
	public static String randomNo() {
		int i = (int)(Math.random()*8999999)+1000000;
		return i+"";
	}

	public static String getUUID() {
		
		String uuid = null;
        try{
    		int j = (int)(Math.random()*900)+100;
    		String strr = j+"";
    		byte[] mac = strr.getBytes();
    		long nanoTime = System.nanoTime();//获取当前时间戳，精确到纳秒。
    		byte[] nanoTimeByte = new byte[8]; 		
    		for(int i = 0;i < 8; ++i){
    			int offset = 64 - (i + 1)*8;
    			nanoTimeByte[i] = (byte)((nanoTime >> offset) & 0xff);
    		}
            uuid = UUID.nameUUIDFromBytes(nanoTimeByte).nameUUIDFromBytes(mac).randomUUID().toString();
        }catch(Exception e){
        	e.printStackTrace();
        }
		return uuid;
	}

}

