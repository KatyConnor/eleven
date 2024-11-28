package hx.nine.eleven.common;

import hx.nine.eleven.commons.enums.FundamentalDataTypeEnums;
import net.sf.cglib.beans.BeanMap;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/11/28
 */
public class BeanUtilsTest {

	public static void main(String[] args) {
		List<String> values = new ArrayList<String>();
		values.add("sdfs");
		values.add("fffff");
		values.add("ssdffff");
		convertTest(values);
	}

	static void convertTest(List<String> values){
		Field[] fields = ObjEntity.class.getDeclaredFields();
		Field field = fields[0];
		if (field.getType().isAssignableFrom(List.class)){
			System.out.println("-----"+field.getType());
		}

		Type[] type = ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments();
		if (FundamentalDataTypeEnums.findMatch(type[0].getTypeName())){
			System.out.println("true----"+type[0].getTypeName());
			ObjEntity obj = new ObjEntity();
			BeanMap beanMap = BeanMap.create(obj);
			beanMap.put(field.getName(),values);
			System.out.println("转换成功"+obj.getList().size());
		}

	}
}
