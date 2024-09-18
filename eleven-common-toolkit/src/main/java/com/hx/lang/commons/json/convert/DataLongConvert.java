package com.hx.lang.commons.json.convert;

import java.util.Date;

/**
 * @author wml
 * @Description
 * @data 2022-06-20
 */
public class DataLongConvert extends FieldConvert<Date> {

    @Override
    public Date convert(Object obj) {
        return new Date(Long.valueOf(String.valueOf(obj)));
    }
}
