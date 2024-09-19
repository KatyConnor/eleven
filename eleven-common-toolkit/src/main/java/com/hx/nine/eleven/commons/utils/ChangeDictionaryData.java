package com.hx.nine.eleven.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 缓存操作
 * @author wangml
 * @Date 2020-06-10
 */
public class ChangeDictionaryData {
	
	//缓存数据初始化
	private static Map<String, Object> cacheData = new HashMap<String, Object>(); 
	
	/**
	 * 
	 * @param dcitionaryName
	 * @param dictionaryData
	 * @param flag
	 * @return
	 * @throws IOException
	 */
	public String doData (String dcitionaryName, String dictionaryData, boolean flag) throws IOException {
		//从缓存中获取该码值数据
		Object obj =  cacheData.get(dcitionaryName);
		//判断是否进入缓存
		if(obj == null) {
			//将数据加入缓存中，并返还映射的数据
			try {
				return getDictionaryDataByparam(dcitionaryName, dictionaryData, flag);
			} catch (IOException e) {
				throw e;
			}
		}else {
			//从缓存中取出映射的文件
			return getDictionaryDataByData(dcitionaryName, dictionaryData, flag);
		}
	}
	
	/**
	 * 
	 * @param param
	 * @param data
	 * @param flag
	 * @return
	 * @throws IOException
	 */
	private String getDictionaryDataByparam(String param, String data, boolean flag) throws IOException {
		if(StringUtils.isBlank(param)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("/conf/prop/change/")
		  .append(param)
		  .append(".properties");
		Properties p = new Properties();
		String dicDatas = null;
		InputStream in = null;
		try {
			in = ChangeDictionaryData.class.getResourceAsStream(sb.toString());
			if(in == null) {
				throw new NullPointerException();
			}
			p.load(in);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			in.close();
		}
		Set<String> keys = p.stringPropertyNames();
		Map<String, Object> dicData = new HashMap<String, Object>();
		//遍历所有的key
		for(String key : keys) {
			//获取Properties文件中key所对应的value;
			Object value = p.get(key);
			dicData.put(key, value);
			//当全流程转换成核心时
			if(flag) {
				if(key.equals(data)) {
					dicDatas = value.toString();
					break;
				}
			}
			//当核心转全流程时
			else {
				if((value.toString()).equals(data)) {
					dicDatas = key;
					break;
				}
			}
		}
		//将数据缓存到集合中
		cacheData.put(param, dicData);
		return dicDatas;		
	}
	
	/**
	 * 
	 * @param param
	 * @param data
	 * @param flag
	 * @return
	 */
	private String getDictionaryDataByData(String param,String data, boolean flag) {
		Object obj = cacheData.get(param);
		String dicDatas = null;
		@SuppressWarnings("unchecked")
		Map<String, Object> mapData = (Map<String, Object>) obj;
		//当全流程转换成核心时
		for(Map.Entry<String, Object> entry : mapData.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			//
			if(flag) {
				if(value.equals(data)) {
					dicDatas = key;
					break;
				}
			}
			//当核心转全流程时
			else {
				if((key.toString()).equals(data)) {
					dicDatas = value;
					break;
				}
			}
		}
		return dicDatas;
	}
}
