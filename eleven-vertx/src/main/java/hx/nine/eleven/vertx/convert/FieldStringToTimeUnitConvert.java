package hx.nine.eleven.vertx.convert;

import hx.nine.eleven.commons.json.convert.FieldConvert;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @auth wml
 * @date 2024/11/6
 */
public class FieldStringToTimeUnitConvert extends FieldConvert<TimeUnit> {

	@Override
	public TimeUnit convert(Object obj) {
		if (Optional.ofNullable(obj).isPresent()) {
			return TimeUnit.valueOf(obj.toString());
		}
		return TimeUnit.SECONDS;
	}
}
