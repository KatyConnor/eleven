package com.hx.nine.eleven.domain.utils;

import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.sys.message.code.code.MessageCode;

public abstract class MessageCodeUtils {

	public static MessageCode format(MessageCode messageCode,String... args){
		messageCode.setMessage( StringUtils.format(messageCode.getMessage(),args));
		 return messageCode;
	}
}
