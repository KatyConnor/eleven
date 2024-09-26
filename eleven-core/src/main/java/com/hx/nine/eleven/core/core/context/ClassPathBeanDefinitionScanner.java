package com.hx.nine.eleven.core.core.context;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.nine.eleven.commons.utils.ObjectUtils;
import com.hx.nine.eleven.core.annotations.Order;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.constant.DefualtProperType;
import com.hx.nine.eleven.core.core.ElevenWebApplicationInitializer;
import com.hx.nine.eleven.core.core.bean.ApplicationBeanRegister;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import com.hx.nine.eleven.core.env.ElevenYamlReadUtils;
import com.hx.nine.eleven.core.exception.ApplicationInitialzerException;
import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import com.hx.nine.eleven.core.factory.ApplicationSubTypesInitFactory;
import com.hx.nine.eleven.core.handler.DefaultHttpRequestServletRouterHandler;
import com.hx.nine.eleven.core.handler.HttpRequestServletRouterHandler;
import com.hx.nine.eleven.core.handler.WebRequestServiceHandler;
import io.vertx.core.json.JsonObject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * package 扫描处理
 *
 * @author wml
 * @date 2022-03-15
 */
public class ClassPathBeanDefinitionScanner {

	public static ClassPathBeanDefinitionScanner build() {
		return new ClassPathBeanDefinitionScanner();
	}

	/**
	 * 扫描项目所有package包，初始化整个依赖类
	 */
	public void initClass() {
		Object packages = ElevenYamlReadUtils.build().getValueMap().get(DefualtProperType.SCAN_PACKAGES);
		String[] scanPackages = packages != null ? stringToArrays(packages) : null;
		Reflections reflections = ObjectUtils.isNotEmpty(scanPackages) ?
				new Reflections(new ConfigurationBuilder().forPackages(scanPackages)) : new Reflections();
		doComponentReflections(reflections);
	}

	/**
	 * 处理应用实例化类
	 *
	 * @param reflections
	 */
	private void doComponentReflections(Reflections reflections) {
		//1、注册bean
		initApplicationBeanRegister(reflections);
		//2、加载指定类的所有子类，并自定义实现
		initApplicationSubTypes(reflections);
		//3、 查找ApplicationAnnotationFactory子类,初始化注解类
		initApplicationAnnotationFactory(reflections);
		//4、加载服务
		initHXVertxWebApplicationInitializer(reflections);
		//5、注册servlet
		initWebRequestServletHandler(reflections);
		//6、httpRequest拦截
		initHttpRequestRouterHandler(reflections);
	}

	/**
	 * 初始化注解类
	 *
	 * @param reflections
	 */
	private void initApplicationAnnotationFactory(Reflections reflections) {
		Set<Class<? extends ApplicationAnnotationFactory>> annotationFactories = reflections.getSubTypesOf(ApplicationAnnotationFactory.class);
		ApplicationContextContainer.build().addBean(ApplicationAnnotationFactory.class.getName(), annotationFactories);
		//排序处理，首先初始化properties，然后再初始化component
		int[] count = {-1};
		annotationFactories.stream().sorted((o1, o2) -> {
			Order order1 = o1.getAnnotation(Order.class);
			Order order2 = o2.getAnnotation(Order.class);
			return Integer.valueOf(order1 == null ? ++count[0] : order1.order())
					.compareTo(order2 == null ? ++count[0] : order2.order());
		}).forEach(classes -> {
			Object obj = ElevenBeanFactory.createBean(classes);
			MethodAccess methodAccess = MethodAccess.get(classes);
			methodAccess.invoke(obj, ConstantType.annotationMethod, reflections);
		});
	}

	private void initHXVertxWebApplicationInitializer(Reflections reflections) {
		Set<Class<? extends ElevenWebApplicationInitializer>> annotationFactories = reflections.getSubTypesOf(ElevenWebApplicationInitializer.class);
		ApplicationContextContainer.build().addBean(ElevenWebApplicationInitializer.class.getName(), annotationFactories);
		// 处理
		annotationFactories.forEach(an -> {
			ConstructorAccess constructorAccess = ConstructorAccess.get(an);
			Object obj = constructorAccess.newInstance();
			MethodAccess methodAccess = MethodAccess.get(an);
			methodAccess.invoke(obj, ConstantType.loadApplication, DefaultElevenApplicationContext.build());
		});
	}


	/**
	 * 注入bean有切入处理
	 *
	 * @param reflections
	 */
	private void initApplicationBeanRegister(Reflections reflections) {
		Set<Class<? extends ApplicationBeanRegister>> annotationFactories = reflections.getSubTypesOf(ApplicationBeanRegister.class);
		Optional.ofNullable(annotationFactories).ifPresent(an -> {
			DefaultElevenApplicationContext.build().addSubTypesOfBeanClass(ApplicationBeanRegister.class, an);
		});
	}

	/**
	 * 加载指定类的所有子类，并自定义实现
	 *
	 * @param reflections
	 */
	private void initApplicationSubTypes(Reflections reflections) {
		Set<Class<? extends ApplicationSubTypesInitFactory>> subTypesOf = reflections.getSubTypesOf(ApplicationSubTypesInitFactory.class);
		Optional.ofNullable(subTypesOf).ifPresent(an -> {
			DefaultElevenApplicationContext.build().addSubTypesOfBeanClass(ApplicationSubTypesInitFactory.class, an);
		});
		// 处理
		subTypesOf.forEach(an -> {
			ConstructorAccess constructorAccess = ConstructorAccess.get(an);
			Object obj = constructorAccess.newInstance();
			MethodAccess methodAccess = MethodAccess.get(an);
			methodAccess.invoke(obj, ConstantType.loadApplication, reflections);
		});
	}

	/**
	 * 初始化web路由拦截处理
	 *
	 * @param reflections
	 */
	private void initWebRequestServletHandler(Reflections reflections) {
		Set<Class<? extends WebRequestServiceHandler>> serviceHandler = reflections.getSubTypesOf(WebRequestServiceHandler.class);
		Optional.ofNullable(serviceHandler).ifPresent(an -> {
			if (an.size() > 1) {
				throw new ApplicationInitialzerException("WebRequestServletHandler 实例不唯一,{}", JsonObject.mapFrom(an).toString());
			}

			if (an.size() == 1) {
				DefaultElevenApplicationContext.build().
						addBean(WebRequestServiceHandler.class.getName(), ElevenBeanFactory.createBean(an.stream().findFirst().get()));
			}
		});
	}

	private void initHttpRequestRouterHandler(Reflections reflections) {
		DefaultElevenApplicationContext.build().
				addBean(DefaultHttpRequestServletRouterHandler.class.getName(), new DefaultHttpRequestServletRouterHandler());
		Set<Class<? extends HttpRequestServletRouterHandler>> routerHandler = reflections.getSubTypesOf(HttpRequestServletRouterHandler.class);
		Optional.ofNullable(routerHandler).ifPresent(tclass -> {
			if (tclass.size() > 0) {
				int[] count = {-1};
				Set<HttpRequestServletRouterHandler> subTypesOfBean = new LinkedHashSet<>();
				tclass.stream().sorted((o1, o2) -> {
					Order order1 = o1.getAnnotation(Order.class);
					Order order2 = o2.getAnnotation(Order.class);
					return Integer.valueOf(order1 == null ? ++count[0] : order1.order())
							.compareTo(order2 == null ? ++count[0] : order2.order());
				}).forEach(c -> {
					if (!c.equals(DefaultHttpRequestServletRouterHandler.class)) {
						subTypesOfBean.add(ElevenBeanFactory.createBean(c));
					}
				});
				DefaultElevenApplicationContext.build().
						addSubTypesOfBean(HttpRequestServletRouterHandler.class, subTypesOfBean);
			}
		});
	}

	public String[] stringToArrays(Object value){
		if (Optional.ofNullable(value).isPresent()){
			return value.toString().split("\\|");
		}
		return null;
	}
}
