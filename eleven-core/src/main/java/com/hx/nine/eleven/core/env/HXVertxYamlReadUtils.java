package com.hx.nine.eleven.core.env;

import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.lang.commons.utils.CollectionUtils;
import com.hx.lang.commons.utils.JSONObjectMapper;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import com.hx.nine.eleven.core.annotations.NestedConfigurationProperty;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.constant.DefualtProperType;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.exception.VertxApplicationRunException;
import com.hx.nine.eleven.core.utils.HXLogger;
import com.hx.nine.eleven.core.utils.SystemUtils;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置文件加载,初始化应用上下文
 *
 * @author wml
 * @date 2023-03-24
 */
public class HXVertxYamlReadUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(HXVertxYamlReadUtils.class);

	private static LinkedHashMap valueMap;

	private HXVertxYamlReadUtils() {
		valueMap = new LinkedHashMap();
	}

	public static HXVertxYamlReadUtils build() {
		return Signle.INSTANCE;
	}

	/**
	 * 加载 yaml 配置文件,将所有的属性值转换成LinkedHashMap对象进行存储
	 * VertxApplicationMain.class.getClassLoader().getResourceAsStream("bootstrap.yml")
	 * Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml")
	 *
	 * @return
	 */
	public HXVertxYamlReadUtils readYamlConfiguration(String configPath) {
		LoaderOptions loadingConfig = new LoaderOptions();
		// 读取默认配置文件
		try (InputStream content = Thread.currentThread().getContextClassLoader().getResourceAsStream(DefualtProperType.DEFAULT_FILE)) {
			loadYaml(loadingConfig, content);
			// 读取其他配置文件
			String activeProfile = String.valueOf(this.valueMap.get(DefualtProperType.ACTIVE_PROFILE));
			if (StringUtils.isBlank(activeProfile)) {
				activeProfile = null;
				LOGGER.warn("获取默认active profile 为空");
			}
			Optional.ofNullable(activeProfile).ifPresent(act -> {
				// 加载其他配置
				try (InputStream activeProfiles = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DefualtProperType.DEFAULT_FILE_NAME + "-" + act + DefualtProperType.SUFFIX)) {
					if (Optional.ofNullable(activeProfiles).isPresent()){
						loadYaml(loadingConfig, activeProfiles);
					}
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			});
			// 读取指定配置文件
			configPath = Optional.ofNullable(configPath).isPresent()?configPath:StringUtils.valueOf(valueMap.get(DefualtProperType.CONFIG_PATH));
			if (ObjectUtils.isNotEmpty(configPath)){
				HXLogger.build(this).info("------加载指定路径的配置文件,[{}]------",configPath);
				String configUrl = StringUtils.valueOf(configPath);
				String[] configUrls = configUrl.split(",");
				for (String urlPath : configUrls){
					// 检查是不是只有一个*. 并且是匹配在文件上
					checkConfigUrl(urlPath);
					// 变量转换
					urlPath = variableConversion(urlPath,valueMap);
					if (urlPath.indexOf("*.") != -1) {
						File[] files = new File(urlPath).listFiles();
						for (File file: files){
							InputStream inputStream = new FileInputStream(file);
							loadYaml(loadingConfig, inputStream);
						}
					}else {
						File file = new File(urlPath);
						InputStream inputStream = new FileInputStream(file);
						loadYaml(loadingConfig, inputStream);
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return this;
	}

	/**
	 * 给Properties对象赋值
	 *
	 * @param propertiesClass
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getProperties(Class<T> propertiesClass, String prefix) {
		Object values = this.valueMap.get(prefix);
		if (!Optional.ofNullable(values).isPresent()) {
			Map<String,Field> nestPropertyMap = this.checkNestProperties(propertiesClass);
			Map<String,Map<String,Object>> propertyMap = new HashMap<>();
			Map<String,ArrayList<Map<String,Object>>> propertyListMap = new HashMap<>();
			// 判断前缀
			Map<String,Object> resultMap = new HashMap<>();
			this.valueMap.forEach((k,v) ->{
				String key = k.toString();
				if (key.startsWith(prefix) && key.lastIndexOf(".") == prefix.length()){
					if (v instanceof ArrayList && ((ArrayList<?>) v).get(0) instanceof LinkedHashMap){
						propertyListMap.put(key,(ArrayList)v);
					}else {
						resultMap.put(key.substring(prefix.length() + 1),v);
					}
				}else {
					int lastIndex = key.lastIndexOf(".");
					if (key.startsWith(prefix) && lastIndex > prefix.length()) {
						// 单个配置
						Map<String,Object> property = propertyMap.get(key.substring(0,lastIndex));
						if (property == null){
							property = new HashMap<>();
						}
						property.put(key.substring(lastIndex+1),v);
						propertyMap.put(key.substring(0,lastIndex),property);
					}
				}
			});

			if (propertyMap.size() > 0){
				propertyMap.forEach((k,v) ->{
					Field field = nestPropertyMap.get(k);
					if (field != null && field.getType().equals(List.class)){
						List<Object> list = new ArrayList<>();
						list.add(v);
						resultMap.put(k.substring(prefix.length()+1),list);
					}else {
						resultMap.put(k.substring(prefix.length()+1),v);
					}
				});
			}

			if (propertyListMap.size() > 0){
				ConcurrentHashMap<Class<?>,List<Field>> nextPropertiesBeans = VertxApplicationContextAware.getBean(ConstantType.CONDITIONAL_ON_NEST_PROPERTITES_BEAN);
				if (ObjectUtils.isEmpty(nextPropertiesBeans)) {
					nextPropertiesBeans = new ConcurrentHashMap<>();
				}
				List<Field> nestFields = new ArrayList<>();
				propertyListMap.forEach((k,v) ->{
					Field field = nestPropertyMap.get(k);
					if (field != null){
						nestFields.add(field);
					}
				});
				nextPropertiesBeans.put(propertiesClass,nestFields);
				DefaultVertxApplicationContext.build().addBean(ConstantType.CONDITIONAL_ON_NEST_PROPERTITES_BEAN, nextPropertiesBeans,true);
			}
			// 赋值

			if (resultMap.size() >0){
				List<T> properties = new ArrayList<>();
				T t = JSONObjectMapper.parseObject(JSONObjectMapper.toJsonString(resultMap),propertiesClass);
				properties.add(t);
				return properties;
			}
			// 检查是否有NestedConfigurationProperty注解
			return null;
		}
		if (values != null && values instanceof List) {
			return bindList(prefix,(List) values, propertiesClass);
		}
		List<T> properties = new ArrayList<>();
		properties.add(bind(values, propertiesClass));
		return properties;
	}

	/**
	 * 加载 yaml 配置文件
	 *
	 * @param vertx
	 */
	public void vertxConfig(Vertx vertx) {
		ConfigStoreOptions store = new ConfigStoreOptions()
				.setType(DefualtProperType.TYPE)
				.setFormat(DefualtProperType.FORMAT)
				.setOptional(DefualtProperType.OPTIONAL)
				.setConfig(new JsonObject().put(DefualtProperType.CLASS_PATH, DefualtProperType.DEFAULT_FILE));
		ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(store));
		retriever.getConfig(prop -> {
			if (prop.succeeded()) {


			} else {
				// 加载失败
			}
		});
	}

	public LinkedHashMap getValueMap() {
		return valueMap;
	}

	public HXVertxYamlReadUtils addProperties(String key,String value){
		valueMap.put(key,value);
		return this;
	}

	public <T> T bind(Object obj, Class<T> target) {
		return JsonObject.mapFrom(obj).mapTo(target);
	}

	public <T> List<T> bindList(String prefix,List obj, Class<T> target) {
		final List<T> bindProperties = new ArrayList<>();
		obj.forEach(l -> {
			Map<String, Object> resultMap = new HashMap<>();
			((Map) l).forEach((k, v) -> {
				resultMap.put(k.toString().substring(prefix.length() + 1), v);
			});
			T p = JsonObject.mapFrom(resultMap).mapTo(target);
			bindProperties.add(p);
		});
		return bindProperties;
	}

	private void loadYaml(LoaderOptions loadingConfig, InputStream content) {
		Yaml activeYaml = new Yaml(new Constructor(loadingConfig));
		Iterable<Object> actives = activeYaml.loadAll(content);
		for (Object active : actives) {
			StringBuilder key = new StringBuilder();
			convert(key, (LinkedHashMap) active, valueMap);
		}
	}

	private void convert(StringBuilder parent, LinkedHashMap valueMap, LinkedHashMap resultMap) {
		String perfix = parent.toString();
		valueMap.forEach((k, v) -> {
			if (parent.length() <= 0) {
				parent.append(perfix);
			}
			if (v instanceof List) {
				List<Map> values = new ArrayList<>(((List<?>) v).size());
				String key = parent.toString();
				((List) v).stream().forEach(p -> {
					LinkedHashMap listMap = new LinkedHashMap<>();
					convert(parent.length() > 0 ? parent.append(".").append(k) : parent.append(k), (LinkedHashMap) p, listMap);
					parent.append(key);
					values.add(listMap);
				});
				resultMap.put(perfix + "." + k, values);
				parent.delete(0, parent.length());
			} else if (v instanceof Map) {
				convert(parent.length() > 0 ? parent.append(".").append(k) : parent.append(k), (LinkedHashMap) v, resultMap);
			} else {
				resultMap.put(parent.append(".").append(k).toString(), v);
				parent.delete(0, parent.length());
			}
		});
	}

	/**
	 * 检查properties属性是否添加NestedConfigurationProperty
	 * @param propertiesClass
	 * @return
	 */
	public Map<String,Field> checkNestProperties(Class<?> propertiesClass) {
		Map<String,Field> subtMap = new HashMap<>();
		try {
			Field[] fields = propertiesClass.getDeclaredFields();
			for (Field field : fields){
				ConfigurationPropertiesBind propertiesBind = null;
				if (!ObjectUtils.isEmpty(field.getAnnotation(NestedConfigurationProperty.class)) &&
						((propertiesBind = field.getType().getAnnotation(ConfigurationPropertiesBind.class))!=null ||
								(propertiesBind = Class.forName(((ParameterizedTypeImpl)field.getGenericType()).
												getActualTypeArguments()[0].getTypeName()).
										getAnnotation(ConfigurationPropertiesBind.class))!=null)){
					subtMap.put(propertiesBind.prefix(),field);
				}
			}
		}catch (ClassNotFoundException exception){
			LOGGER.error("",exception);
		}
		return subtMap;
	}

	private void checkConfigUrl(String configUrl){
		if (configUrl.indexOf("*") == -1){
			return;
		}
		int index = configUrl.indexOf("*");
		int lastIndex = configUrl.lastIndexOf("*");
		int lastFile = configUrl.lastIndexOf(SystemUtils.osName()==1?"\\":"/");
		if (lastIndex != index || configUrl.indexOf("*.") == -1 || lastFile > lastIndex){
			// 抛出异常，应用停止
			throw new VertxApplicationRunException("config url 路径中只能配置一个 ‘*’ ，且只能模糊匹配文件，如：/home/app/config/*.yml");
		}
	}

	private String variableConversion(String configUrl,Map<String, Object> valueMap){
		if (configUrl.indexOf("{") != -1 && configUrl.indexOf("}") != -1){
			return StringUtils.format(configUrl,valueMap);
		}
		return configUrl;
	}

	private final static class Signle {
		public final static HXVertxYamlReadUtils INSTANCE = new HXVertxYamlReadUtils();
	}
}
