package com.hx.vertx.boot.core.context;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.boot.annotations.Order;
import com.hx.vertx.boot.constant.ConstantType;
import com.hx.vertx.boot.constant.DefualtProperType;
import com.hx.vertx.boot.core.HXVertxWebApplicationInitializer;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.core.bean.ApplicationBeanRegister;
import com.hx.vertx.boot.core.bean.VertxBeanFactory;
import com.hx.vertx.boot.env.HXVertxYamlReadUtils;
import com.hx.vertx.boot.exception.ApplicationInitialzerException;
import com.hx.vertx.boot.factory.ApplicationAnnotationFactory;
import com.hx.vertx.boot.handler.DefaultHttpRequestServletRouterHandler;
import com.hx.vertx.boot.handler.HttpRequestServletRouterHandler;
import com.hx.vertx.boot.handler.WebRequestServiceHandler;
import com.hx.vertx.boot.properties.VertxApplicationProperties;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.util.HashSet;
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
		Object packages = HXVertxYamlReadUtils.build().getValueMap().get(DefualtProperType.SCAN_PACKAGES);
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
		//2、 查找ApplicationAnnotationFactory子类,初始化注解类
		initApplicationAnnotationFactory(reflections);
		//3、加载服务
		initHXVertxWebApplicationInitializer(reflections);
		//4、注册servlet
		initWebRequestServletHandler(reflections);
		//5、httpRequest拦截
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
			Object obj = VertxBeanFactory.createBean(classes);
			MethodAccess methodAccess = MethodAccess.get(classes);
			methodAccess.invoke(obj, ConstantType.annotationMethod, reflections);
		});
	}

	private void initHXVertxWebApplicationInitializer(Reflections reflections) {
		Set<Class<? extends HXVertxWebApplicationInitializer>> annotationFactories = reflections.getSubTypesOf(HXVertxWebApplicationInitializer.class);
		ApplicationContextContainer.build().addBean(HXVertxWebApplicationInitializer.class.getName(), annotationFactories);
		// 处理
		annotationFactories.forEach(an -> {
			ConstructorAccess constructorAccess = ConstructorAccess.get(an);
			Object obj = constructorAccess.newInstance();
			MethodAccess methodAccess = MethodAccess.get(an);
			methodAccess.invoke(obj, ConstantType.loadApplication, DefaultVertxApplicationContext.build());
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
			DefaultVertxApplicationContext.build().addSubTypesOfBeanClass(ApplicationBeanRegister.class, an);
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
				DefaultVertxApplicationContext.build().
						addBean(WebRequestServiceHandler.class.getName(), VertxBeanFactory.createBean(an.stream().findFirst().get()));
			}
		});
	}

	private void initHttpRequestRouterHandler(Reflections reflections) {
		DefaultVertxApplicationContext.build().
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
						subTypesOfBean.add(VertxBeanFactory.createBean(c));
					}
				});
				DefaultVertxApplicationContext.build().
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
