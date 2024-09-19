package com.hx.nine.eleven.thread.pool.executor.convert;

import com.hx.nine.eleven.commons.json.convert.FieldConvert;

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
         return 0l;
    }
}
