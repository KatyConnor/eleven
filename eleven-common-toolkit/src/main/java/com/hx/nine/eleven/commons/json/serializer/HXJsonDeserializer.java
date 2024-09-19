package com.hx.nine.eleven.commons.json.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wml
 * @Description
 * @data 2022-06-16
 */
public abstract class HXJsonDeserializer<T> extends JsonDeserializer<T> {


    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt, T intoValue) throws IOException, JacksonException {
        return super.deserialize(p, ctxt, intoValue);
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        jsonParser.getTypeId()
//        jsonParser.readValueAs()
        return null;
    }
}
