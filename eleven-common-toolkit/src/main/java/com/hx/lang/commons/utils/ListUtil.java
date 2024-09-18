package com.hx.lang.commons.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ListUtil {
	
	public static <E> List<E> stringToList(String string) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		List<E> tempValue = objectMapper.readValue(string, List.class);
		return tempValue;
		
	}
	
	public static <E> String listToString(List<E> list) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(list);
	}

	/**
	 * 获取map中value最小对应的key值
	 * 
	 */
	public static String getMaxStr(Map<String, Integer> map) {

		int maxV = 0;
		String maxK = null;
		String maxK_mayberemove = null;// 中间值，用于保存每次存在的最大值键，但存在下个值比他大，可用他移除掉，替换成最新的值
		Map<String, Integer> map2 = new TreeMap<String, Integer>();
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			maxK = key.toString();
			int value = map.get(key);
			if (value > maxV) {
				if (maxK_mayberemove != null) {
					map2.clear();
				}
				maxV = value;
				map2.put(maxK, maxV);
				maxK_mayberemove = maxK;
			} else if (value == maxV) {
				map2.put(maxK, maxV);
			}
		}

		Iterator<String> keys2 = map2.keySet().iterator();
		// StringBuffer buffer = new StringBuffer();
		while (keys2.hasNext()) {
			Object key = keys2.next();
			maxK = key.toString();
			// int value = map.get(key);
			// String[] maxKey = maxK.split("_");
			// buffer.append(maxKey[0].replace("\"", "") + "：" + value + "</br>");
		}

		return maxK;
	}
}
