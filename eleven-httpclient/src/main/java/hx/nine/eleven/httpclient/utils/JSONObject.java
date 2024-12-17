package hx.nine.eleven.httpclient.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author wml
 * 2020-06-08
 */
public class JSONObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObject.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJSONString(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("parse object to json sring exception [{}]",e);
        }
        return null;
    }

    public static <T> T parseObject(String jsonString,Class<T> calsszz){
        try {
            return mapper.readValue(jsonString,calsszz);
        } catch (IOException e) {
            LOGGER.error("parse json string to json Object exception [{}]",e);
        }
        return null;
    }

    public static Map<String,Object> parseObjectToMap(Object jsonObj){
        try {
            String str = mapper.writeValueAsString(jsonObj);
            return mapper.readValue(str,Map.class);
        } catch (IOException e) {
            LOGGER.error("parse object to Map sring exception [{}]",e);
        }

        return null;
    }

    public static Map<String,Object> parseObjectToMap(String string){
        try {
            return mapper.readValue(string,Map.class);
        } catch (IOException e) {
            LOGGER.error("parse object to Map sring exception [{}]",e);
        }

        return null;
    }
}
