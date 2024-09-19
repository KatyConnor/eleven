package com.hx.nine.eleven.commons.json.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataTimeDefaultDeserializer extends JsonDeserializer<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTimeDefaultDeserializer.class);
    public static final DataTimeDefaultDeserializer INSTANCE = new DataTimeDefaultDeserializer();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        try {
            if (JsonToken.VALUE_STRING == jsonParser.getCurrentToken()){
                return dateFormat.parse(jsonParser.getText().trim());
            }
            long longvalue = jsonParser.getLongValue();
            if (longvalue > 0){
                return  new Date(longvalue);
            }
        } catch (ParseException ex) {
            LOGGER.error("date fomat failed , exception: {}",ex);
        }
        return null;
    }
}
