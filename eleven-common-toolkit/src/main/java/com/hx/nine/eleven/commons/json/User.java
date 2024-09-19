package com.hx.nine.eleven.commons.json;

import com.hx.nine.eleven.commons.json.convert.DataLongConvert;
import com.hx.nine.eleven.commons.json.convert.IntegerAndBigDecimalConvert;
import com.hx.nine.eleven.commons.annotation.FieldTypeConvert;
import com.hx.nine.eleven.commons.json.serializer.BigDecimalDeserialier;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wml
 * @Description
 * @data 2022-06-20
 */
public class User {

    private String name;
    private int age;
    @JsonDeserialize(using = BigDecimalDeserialier.class)
    @FieldTypeConvert(using = IntegerAndBigDecimalConvert.class)
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FieldTypeConvert(using = DataLongConvert.class)
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
