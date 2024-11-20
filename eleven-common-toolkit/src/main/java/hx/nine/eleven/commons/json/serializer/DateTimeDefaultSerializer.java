package hx.nine.eleven.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeDefaultSerializer extends JsonSerializer<Date> {

    public static final DateTimeDefaultSerializer INSTANCE = new DateTimeDefaultSerializer();

    public DateTimeDefaultSerializer(){}

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    }
}
