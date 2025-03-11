package hx.nine.eleven.commons.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author wml
 * @Date 2019-08-17
 */
@JsonSerialize( using = EnumSerializer.class )
public interface BaseEnum<E extends Enum<?>, K> {

	/**
	 * @return
	 */
	K getValue();

	/**
	 * @return
	 */
	E getName();

	/**
	 * @return
	 */
	String getEnumName();
}