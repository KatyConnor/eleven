package hx.nine.eleven.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Object 对象处理常用工具类
 *
 * @Author wml
 * @Date 2017-08-30 17:57
 */

public class ObjectUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 对象转成 string
	 *
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return JSONObjectMapper.toJsonString(obj);
	}

	/**
	 * 循环 bean 转换成循环的 map
	 *
	 * @param listBeans
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static List<Map<String, Object>> convertListBean(List listBeans) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<Map<String, Object>> listMap = new ArrayList();
		if (listBeans == null) {
			return null;
		}

		for (Object bean : listBeans) {
			Map<String, Object> map = convertBean(bean);
			listMap.add(map);
		}
		return listMap;
	}


	/**
	 * 将一个 JavaBean 对象转化为一个  Map
	 *
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 * @throws IntrospectionException    如果分析类属性失败
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException    如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> convertBean(Object bean) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			String firstChar = String.valueOf(propertyName.charAt(0));
			if (!propertyName.equals("class")) {
				// 首字母大写
				propertyName = propertyName.replaceFirst(firstChar, firstChar.toUpperCase());
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
/*                    if(result instanceof Date){
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                        result = df.format(result);
                    }*/
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> convertBeanLowe(Object bean) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			String firstChar = String.valueOf(propertyName.charAt(0));
			if (!propertyName.equals("class")) {
				// 首字母小写
				propertyName = propertyName.replaceFirst(firstChar, firstChar.toLowerCase());
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}


	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 *
	 * @param type 要转化的类型
	 * @param map  包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException    如果分析类属性失败
	 * @throws IllegalAccessException    如果实例化 JavaBean 失败
	 * @throws InstantiationException    如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map map) throws IntrospectionException, IllegalAccessException,
			InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 属性赋值
				try {
					Object value = map.get(propertyName);

					Object[] args = new Object[1];
					args[0] = value;

					descriptor.getWriteMethod().invoke(obj, args);
				} catch (Exception e) {
					// TODO: Timestamp 类型时赋值会有问题,直接跳过
				}
			}
		}
		return obj;
	}

	/**
	 * 将前端传入的 Map 参数封装成Map <br/>
	 * 注：当传入的Map中参数首字母为大写时调用此方法
	 *
	 * @param paramsMap 前端传入的Map参数
	 * @return 封装出来的 map 参数 对象
	 */
	public static Map<String, Object> getParamsMap(Map<String, Object> paramsMap) {

		Map<String, Object> paraMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			String propertyName = entry.getKey();
			String firstChar = String.valueOf(propertyName.charAt(0));
			// (因前端传入参数为首字母大写) so将首字母转为小写
			propertyName = propertyName.replaceFirst(firstChar, firstChar.toLowerCase());

			if (entry.getValue() != null) {
				if (entry.getValue() instanceof String) {
					if (!StringUtils.isEmpty(String.valueOf(entry.getValue()))) {
						paraMap.put(propertyName, entry.getValue());
					}
				} else {
					paraMap.put(propertyName, entry.getValue());
				}
			}
		}
		return paraMap;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个  Map value为String类型
	 *
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 * @throws IntrospectionException    如果分析类属性失败
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException    如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> convertBeanString(Object bean) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			String firstChar = String.valueOf(propertyName.charAt(0));
			if (!propertyName.equals("class")) {
				// 首字母大写
				propertyName = propertyName.replaceFirst(firstChar, firstChar.toUpperCase());
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result.toString());
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/**
	 * 非空对象相等判断
	 *
	 * @param o1 对象1
	 * @param o2 对象2
	 * @return
	 */
	public static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		} else if (o1 != null && o2 != null) {
			if (o1.equals(o2)) {
				return true;
			} else {
				return o1.getClass().isArray() && o2.getClass().isArray() ? arrayEquals(o1, o2) : false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 计算对象的 HashCode
	 * @param obj
	 * @return
	 */
	public static int nullSafeHashCode(Object obj) {
		if (obj == null) {
			return 0;
		} else {
			if (obj.getClass().isArray()) {
				if (obj instanceof Object[]) {
					return nullSafeHashCode((Object[]) ((Object[]) obj));
				}

				if (obj instanceof boolean[]) {
					return nullSafeHashCode((boolean[]) ((boolean[]) obj));
				}

				if (obj instanceof byte[]) {
					return nullSafeHashCode((byte[]) ((byte[]) obj));
				}

				if (obj instanceof char[]) {
					return nullSafeHashCode((char[]) ((char[]) obj));
				}

				if (obj instanceof double[]) {
					return nullSafeHashCode((double[]) ((double[]) obj));
				}

				if (obj instanceof float[]) {
					return nullSafeHashCode((float[]) ((float[]) obj));
				}

				if (obj instanceof int[]) {
					return nullSafeHashCode((int[]) ((int[]) obj));
				}

				if (obj instanceof long[]) {
					return nullSafeHashCode((long[]) ((long[]) obj));
				}

				if (obj instanceof short[]) {
					return nullSafeHashCode((short[]) ((short[]) obj));
				}
			}

			return obj.hashCode();
		}
	}

	public static int nullSafeHashCode(Object[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			Object[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				Object element = var2[var4];
				hash = 31 * hash + nullSafeHashCode(element);
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(boolean[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			boolean[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				boolean element = var2[var4];
				hash = 31 * hash + Boolean.hashCode(element);
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(byte[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			byte[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				byte element = var2[var4];
				hash = 31 * hash + element;
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(char[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			char[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				char element = var2[var4];
				hash = 31 * hash + element;
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(double[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			double[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				double element = var2[var4];
				hash = 31 * hash + Double.hashCode(element);
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(float[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			float[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				float element = var2[var4];
				hash = 31 * hash + Float.hashCode(element);
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(int[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			int[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				int element = var2[var4];
				hash = 31 * hash + element;
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(long[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			long[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				long element = var2[var4];
				hash = 31 * hash + Long.hashCode(element);
			}

			return hash;
		}
	}

	public static int nullSafeHashCode(short[] array) {
		if (array == null) {
			return 0;
		} else {
			int hash = 7;
			short[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				short element = var2[var4];
				hash = 31 * hash + element;
			}

			return hash;
		}
	}

	private static boolean arrayEquals(Object o1, Object o2) {
		if (o1 instanceof Object[] && o2 instanceof Object[]) {
			return Arrays.equals((Object[]) ((Object[]) o1), (Object[]) ((Object[]) o2));
		} else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
			return Arrays.equals((boolean[]) ((boolean[]) o1), (boolean[]) ((boolean[]) o2));
		} else if (o1 instanceof byte[] && o2 instanceof byte[]) {
			return Arrays.equals((byte[]) ((byte[]) o1), (byte[]) ((byte[]) o2));
		} else if (o1 instanceof char[] && o2 instanceof char[]) {
			return Arrays.equals((char[]) ((char[]) o1), (char[]) ((char[]) o2));
		} else if (o1 instanceof double[] && o2 instanceof double[]) {
			return Arrays.equals((double[]) ((double[]) o1), (double[]) ((double[]) o2));
		} else if (o1 instanceof float[] && o2 instanceof float[]) {
			return Arrays.equals((float[]) ((float[]) o1), (float[]) ((float[]) o2));
		} else if (o1 instanceof int[] && o2 instanceof int[]) {
			return Arrays.equals((int[]) ((int[]) o1), (int[]) ((int[]) o2));
		} else if (o1 instanceof long[] && o2 instanceof long[]) {
			return Arrays.equals((long[]) ((long[]) o1), (long[]) ((long[]) o2));
		} else {
			return o1 instanceof short[] && o2 instanceof short[] ? Arrays.equals((short[]) ((short[]) o1), (short[]) ((short[]) o2)) : false;
		}
	}

	/**
	 * 对象非空判断，如果是空对象则给一个默认值
	 * @param object
	 * @param defaultValue
	 * @param <T>
	 * @param <S>
	 * @return
	 */
	public static <T, S extends T> T defaultIfNull(T object, S defaultValue) {
		return isEmpty(object)? defaultValue : object;
	}

	public static boolean isArray(Object obj) {
		return obj != null && obj.getClass().isArray();
	}

	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			return StringUtils.isEmpty(obj);
		} else if (obj instanceof Optional) {
			return !((Optional) obj).isPresent();
		} else if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		} else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else {
			return obj instanceof Map ? ((Map) obj).isEmpty() : false;
		}
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * @param baseEntity 要转化的类型
	 * @param map 包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
//    @SuppressWarnings("rawtypes")
//    public static void convertMap(BaseEntity baseEntity, Map map)
//            throws IntrospectionException {
//        BeanInfo beanInfo = Introspector.getBeanInfo(baseEntity.getClass()); // 获取类属性
//
//        // 给 JavaBean 对象的属性赋值
//        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
//        for (int i = 0; i< propertyDescriptors.length; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            String firstChar = String.valueOf(propertyName.charAt(0));
//            propertyName = propertyName.replaceFirst(firstChar, firstChar.toUpperCase());
//            if (map.containsKey(propertyName)) {
//                // 属性赋值
//                try {
//                    Object value = map.get(propertyName);
//
//                    Object[] args = new Object[1];
//                    args[0] = value;
//
//                    descriptor.getWriteMethod().invoke(baseEntity, args);
//                } catch (Exception e) {
//                    // TODO: Timestamp 类型时赋值会有问题,直接跳过
//                }
//            }
//        }
//    }
//
//    /**
//     * 将前端传入的参数封装成Map
//     * @param reqBody 前端传入的参数
//     * @return 封装出来的 map 参数 对象
//     */
//    public static Map<String, Object> getParamsMap(Context<String, Object> reqBody) {
//
//        Map<String, Object> paraMap = new HashMap<String, Object>();
//        for (Map.Entry<String, Object> entry : reqBody.entrySet()) {
//            String propertyName = entry.getKey();
//            String firstChar = String.valueOf(propertyName.charAt(0));
//            // (因前端传入参数为首字母大写) so将首字母转为小写
//            propertyName = propertyName.replaceFirst(firstChar, firstChar.toLowerCase());
//
//            paraMap.put(propertyName, entry.getValue());
//        }
//        return paraMap;
//    }
}
