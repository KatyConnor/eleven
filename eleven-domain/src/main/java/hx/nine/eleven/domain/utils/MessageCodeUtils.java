package hx.nine.eleven.domain.utils;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.msg.code.code.MessageCode;

public abstract class MessageCodeUtils {

	public static MessageCode format(MessageCode messageCode, String... args){
		messageCode.setMessage( StringUtils.format(messageCode.getMessage(),args));
		 return messageCode;
	}
}
