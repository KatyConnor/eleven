package com.hx.lang.commons.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author wml
 * @Date 2019-04-17
 */
public class EnumSerializer extends JsonSerializer<BaseEnum> {
	@Override
	public void serialize( BaseEnum value, JsonGenerator jgen, SerializerProvider provider ) throws IOException {
		if ( null != value ) {
			jgen.writeStartObject();
			jgen.writeFieldName( "value" );
			jgen.writeObject( value.getValue() );
			jgen.writeFieldName( "name" );
			jgen.writeObject( value.getEnumName() );
			jgen.writeEndObject();
		} else {
			jgen.writeNull();
		}
	}

}