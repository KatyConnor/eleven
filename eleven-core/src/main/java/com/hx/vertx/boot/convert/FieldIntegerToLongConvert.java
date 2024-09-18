package com.hx.vertx.boot.convert;

import com.hx.lang.commons.json.convert.FieldConvert;

import java.util.Optional;

public class FieldIntegerToLongConvert extends FieldConvert<Long> {
	@Override
	public Long convert(Object obj) {
		if (Optional.ofNullable(obj).isPresent()){
			if (Integer.class.isAssignableFrom(obj.getClass())){
				return Long.valueOf(obj.toString());
			}

			if (Long.class.isAssignableFrom(obj.getClass())){
				return (Long) obj;
			}
		}
		return null;
	}
}
