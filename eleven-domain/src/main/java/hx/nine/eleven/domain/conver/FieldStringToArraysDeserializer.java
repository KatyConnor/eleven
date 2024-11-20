package hx.nine.eleven.domain.conver;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class FieldStringToArraysDeserializer extends JsonDeserializer<String[]> {

	@Override
	public String[] deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
		String value = jsonParser.getValueAsString();
		if (Optional.ofNullable(value).isPresent()){
			return value.split("\\|");
		}
		return new String[0];
	}
}
