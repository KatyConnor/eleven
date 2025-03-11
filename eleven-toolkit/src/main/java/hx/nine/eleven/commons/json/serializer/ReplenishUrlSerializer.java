package hx.nine.eleven.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ReplenishUrlSerializer extends JsonSerializer<String> {

    //Http根地址
    private String httpUrl;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(s != null && !s.isEmpty()){
            //拼接Http根地址与value值
            jsonGenerator.writeString(this.httpUrl+s);
        }
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
}
