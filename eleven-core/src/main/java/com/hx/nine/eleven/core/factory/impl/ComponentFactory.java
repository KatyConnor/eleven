package com.hx.nine.eleven.core.factory.impl;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.lang.commons.utils.*;
import com.hx.nine.eleven.core.annotations.*;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import com.hx.nine.eleven.core.core.bean.VertxBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.core.entity.BeanResourceEntity;
import com.hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * component注解类加载
 *
 * @author wml
 * @date 2023-03-24
 */
@Order(order = -9990)
public class ComponentFactory implements ApplicationAnnotationFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(ComponentFactory.class);

	public static void main(String[] args) {
		Integer[] ints = new Integer[]{-1024, -22, 33, 26, 19, 26, 1, 0, 3, 8};
		List<Integer> intList = Arrays.asList(ints);
		intList.sort((o1, o2) -> {
			return o1.compareTo(o2);
		});
		intList.forEach(i -> {
			System.out.println(i);
		});
	}

	@Override
	public void loadAnnotations(Reflections reflections) {
		Set<Class<?>> annotationSet = reflections.getTypesAnnotatedWith(Component.class);
		Set<Class<?>> subComponentSet = reflections.getTypesAnnotatedWith(SubComponent.class);
		initBean(subComponentSet, true);  // 初始化子类，存储主键为父类
		initBean(annotationSet, false);
		// 初始化有依赖的类
		afterBeanCreate();
	}

	/**
	 * 初始化bean,先通过order进行排序，然后进行循环初始化
	 *
	 * @param annotationSet
	 * @param isSub
	 */
	private void initBean(Set<Class<?>> annotationSet, boolean isSub) {
		Optional.ofNullable(annotationSet).ifPresent(classList -> {
			int[] count = {-1};
			classList.stream().sorted((o1, o2) -> {
				// 排序判断,默认根据order判断，从小到大进行排序，如果没有order注解则直接默认顺序
				Order order1 = o1.getAnnotation(Order.class);
				Order order2 = o2.getAnnotation(Order.class);
				return Integer.valueOf(order1 == null ? ++count[0] : order1.order())
						.compareTo(order2 == null ? ++count[0] : order2.order());
			}).forEach(bean -> {
				try {
					if (bean.getAnnotation(Target.class) != null ||
							bean.getAnnotation(Retention.class) != null ||
							bean.getAnnotation(Documented.class) != null ||
							bean.getAnnotation(Inherited.class) != null) {
						LOGGER.warn("[{}]注解跳过初始化", bean.getName());
					} else if (checkProperty(bean)) {
						createBean(bean, isSub);
					}
				} catch (Exception e) {
					LOGGER.error("@annotation Component注解 [{}] 注册容器异常",bean.getName());
					LOGGER.error("exception：{}",e);
					System.exit(0);
				}
			});
		});
	}

	/**
	 * 所有bean创建完之后，扫描依赖条件的class进行初始化
	 */
	private void afterBeanCreate() {
		ConcurrentLinkedQueue<Class<?>> afterBeansQueue = VertxApplicationContextAware.
				getBean(ConstantType.CONDITIONAL_ON_AFTER_BEAN);
		Optional.ofNullable(afterBeansQueue).ifPresent(beans -> {
			Class<?> bean = afterBeansQueue.peek();
			while (bean != null) {
				SubComponent subComponent = bean.getAnnotation(SubComponent.class);
				createBean(bean, subComponent != null);
				afterBeansQueue.remove(bean);
				bean = afterBeansQueue.peek();
			}
		});

		ConcurrentLinkedQueue<BeanResourceEntity> resourceBeansQueue = VertxApplicationContextAware.
				getBean(ConstantType.RESOURCE_BEAN_ON_AFTER_BEAN);
		Optional.ofNullable(resourceBeansQueue).ifPresent(beans -> {
			BeanResourceEntity bean = resourceBeansQueue.peek();
			while (bean != null) {
				Field field = bean.getField();
				Object obj = VertxApplicationContextAware.getBean(bean.getObj());
				field.setAccessible(true);
				try {
					field.set(obj,VertxApplicationContextAware.getBean(field.getType()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				resourceBeansQueue.remove(bean);
				bean = resourceBeansQueue.peek();
			}
		});
	}

	private void createBean(Class<?> bean, boolean isSub) {
		Component component = bean.getAnnotation(Component.class);
		MethodAccess methodAccess = null;
		Annotation an = null;
		if (ObjectUtils.isEmpty(component)){
			 Annotation[] annotations = bean.getAnnotations();
			for (Annotation a : annotations){
				 if (a.annotationType().getAnnotation(Component.class) != null){
					 methodAccess = MethodAccess.get(a.getClass());
					 an = a;
					 break;
				 }
			 }
		}

		if (!ObjectUtils.isEmpty(component) || !ObjectUtils.isEmpty(methodAccess)){
			String value = !ObjectUtils.isEmpty(component)?component.value():StringUtils.valueOf(methodAccess.invoke(an,"value"));
			String methodInit = !ObjectUtils.isEmpty(component)?component.init():StringUtils.valueOf(methodAccess.invoke(an,"init"));
			if (StringUtils.isNotEmpty(value) && DefaultVertxApplicationContext
					.build().getBean(value) != null ||
					DefaultVertxApplicationContext.build().getBean(bean) != null) {
				LOGGER.warn("容器中已经初始化[{}]对象[]",!ObjectUtils.isEmpty(value)?value:bean.getName(),bean.getName());
				return;
			}

			Object obj = VertxBeanFactory.initBean(bean);
			if (obj != null) {
				// 设置属性@Resource
				setField(bean, obj, isSub);
				// 调用初始化方法
				if (StringUtils.isNotBlank(methodInit)) {
					VertxBeanFactory.invoke(methodInit, obj);
				}
				//添加到容器
				if (StringUtils.isNotEmpty(value)) {
					DefaultVertxApplicationContext.build().addBean(value, obj);
				} else {
					if (isSub) {
						Class<?> interfaces = (Class<?>) methodAccess.invoke(an,"interfaces");
						DefaultVertxApplicationContext.build().addBean(interfaces.getName(),obj);
					} else {
						DefaultVertxApplicationContext.build().addBean(obj);
					}
				}
				LOGGER.info("注册bean=[{}]", bean.getSimpleName());
			}
		};
	}

	/**
	 * 检查类是否添加ConditionalOnProperty注解，如果添加则需要判断条件是否满足
	 * 如果没有ConditionalOnProperty或者条件全部满足返回TRUE否则返回FALSE
	 *
	 * @param bean
	 * @return 返回true/false
	 */
	private boolean checkProperty(Class<?> bean) {
		ConditionalOnProperty property = bean.getAnnotation(ConditionalOnProperty.class);
		if (property != null) {
			String prefix = property.prefix();
			String havingValue = property.havingValue();
			String[] names = property.name();
			int count = 0;
			ApplicationEnvConfigProperty envConfigProperty = DefaultVertxApplicationContext.build().getProperties();
			for (String key : names) {
				Object value = envConfigProperty.get(prefix + key);
				if (value == null){
					Object propertyObj = envConfigProperty.getProperty(property.propertiesClass());
					if (propertyObj != null){
						value = BeanMapUtil.beanToMap(propertyObj).get(key);
					}
				}
				if (StringUtils.isNotBlank(havingValue) && havingValue.equalsIgnoreCase(StringUtils.valueOf(value))) {
					count++;
				}
			}
			return count == names.length;
		}
		return true;
	}

	/**
	 * @param bean
	 * @param obj
	 * @param isSub
	 * @TODO 此处有一个依赖创建bug需要修复
	 * 1、检查容器中是否存在，如果存在直接取出赋值
	 * 2、容器中不存在，需要创建bean，并放入容器（按照创建bean检查）
	 */
	private void setField(Class<?> bean, Object obj, boolean isSub) {
		FieldAccess fieldAccess = FieldAccess.get(bean);
		Field[] fields = bean.getDeclaredFields();
		for (Field field : fields) {
			Resource resource = field.getAnnotation(Resource.class);
			Optional.ofNullable(resource).ifPresent(f -> {
				Class<?> fieldTypeClass = field.getType();
				Object fieldObj = VertxApplicationContextAware.getBean(fieldTypeClass);
				if (fieldObj == null && checkProperty(fieldTypeClass)) {
					SubComponent subComponent = fieldTypeClass.getAnnotation(SubComponent.class);
					createBean(fieldTypeClass, subComponent != null);
				}
				// 判断属性类型时properties还是普通类
				fieldObj = ObjectUtils.isEmpty(fieldTypeClass.getAnnotation(ConfigurationPropertiesBind.class))?
						VertxApplicationContextAware.getBean(fieldTypeClass):VertxApplicationContextAware.getProperties(fieldTypeClass);
				if (fieldObj == null) {
					BeanResourceEntity resourceEntity = new BeanResourceEntity(field, obj.getClass().getName());
					ConcurrentLinkedQueue<BeanResourceEntity> resourceBeansQueue = VertxApplicationContextAware.getBean(ConstantType.RESOURCE_BEAN_ON_AFTER_BEAN);
					if (CollectionUtils.isEmpty(resourceBeansQueue)) {
						resourceBeansQueue = new ConcurrentLinkedQueue();
					}
					resourceBeansQueue.add(resourceEntity);
					DefaultVertxApplicationContext.build().addBean(ConstantType.RESOURCE_BEAN_ON_AFTER_BEAN, resourceBeansQueue,true);
					LOGGER.warn("[{}] 没有实例化",fieldTypeClass.getName());
					return;
				}
				field.setAccessible(true);
				try {
					field.set(obj,fieldObj);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			});
		}
	}

}
