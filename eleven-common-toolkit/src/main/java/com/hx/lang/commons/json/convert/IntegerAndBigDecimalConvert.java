package com.hx.lang.commons.json.convert;

import java.math.BigDecimal;

/**
 * @author wml
 * @Description
 * @data 2022-06-20
 */
public class IntegerAndBigDecimalConvert extends FieldConvert<BigDecimal> {

    @Override
    public BigDecimal convert(Object obj) {
        return new BigDecimal(String.valueOf(obj)).setScale(6,BigDecimal.ROUND_HALF_UP);
    }
}
