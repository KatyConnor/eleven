package com.hx.domain.framework.autoconfig;

import com.hx.domain.framework.DomainAnnotationUtils;
import com.hx.domain.framework.enums.WebRouteParamsEnums;
import com.hx.domain.framework.web.DomainApplicationContainer;
import com.hx.domain.framework.annotations.domain.support.MapperFactory;
import com.hx.domain.framework.annotations.domain.support.WebBodyDTO;
import com.hx.domain.framework.annotations.domain.support.WebBodyForm;
import com.hx.domain.framework.annotations.domain.support.WebBodyParam;
import com.hx.domain.framework.annotations.domain.support.WebHeaderDTO;
import com.hx.domain.framework.annotations.domain.support.WebHeaderForm;
import com.hx.domain.framework.annotations.domain.support.WebHeaderParam;
import com.hx.domain.framework.annotations.domain.support.WebHeaderVO;
import com.hx.vertx.boot.factory.ApplicationAnnotationFactory;
import io.vertx.core.json.JsonArray;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 加载初始化拥有注解的bean对象
 * @author wml
 * @date 2023-03-29
 */
public class DomainApplicationAnnotationFactory implements ApplicationAnnotationFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainApplicationAnnotationFactory.class);

	@Override
	public void loadAnnotations(Reflections reflections) {
		List<Class<Annotation>> annotations = DomainAnnotationUtils.values();
		LOGGER.info("初始化[{}]", JsonArray.of(annotations).toString());
		annotations.stream().forEach(a -> {
			Set<Class<?>> anntationSet = reflections.getTypesAnnotatedWith(a);
			Optional.ofNullable(anntationSet).ifPresent(classList -> {
				String type = a.getSimpleName();
				switch (type) {
					case "WebHeaderForm":
						addWebHeaderFormBean(anntationSet);
					case "WebBodyForm":
						addWebBodyFormBean(anntationSet);
					case "WebHeaderDTO":
						addWebHeaderDTOBean(anntationSet);
					case "WebBodyDTO":
						addWebBodyDTOBean(anntationSet);
					case "WebHeaderParam":
						addWebHeaderParamBean(anntationSet);
					case "WebBodyParam":
						addWebBodyParamBean(anntationSet);
					case "MapperFactory":
						addMapperFactoryBean(anntationSet);
					case "WebHeaderVO":
						addWebHeaderVOBean(anntationSet);
					default:
				}
			});

		});
	}

	public void addWebHeaderFormBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebHeaderForm webHeaderForm = v.getAnnotation(WebHeaderForm.class);
				Optional.ofNullable(webHeaderForm).ifPresent(form -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.HEADER_FORM.getName());
					key.append(webHeaderForm.value());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebHeaderForm 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebBodyFormBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebBodyForm webBodyForm = v.getAnnotation(WebBodyForm.class);
				Optional.ofNullable(webBodyForm).ifPresent(form -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.BODY_FORM.getName());
					key.append(webBodyForm.tradeCode()).append(webBodyForm.subTradeCode());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebBodyForm 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebHeaderDTOBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebHeaderDTO webHeaderDTO = v.getAnnotation(WebHeaderDTO.class);
				Optional.ofNullable(webHeaderDTO).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.HEADER_DTO.getName());
					key.append(webHeaderDTO.value());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebHeaderDTO 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebBodyDTOBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebBodyDTO webBodyDTO = v.getAnnotation(WebBodyDTO.class);
				Optional.ofNullable(webBodyDTO).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.BODY_DTO.getName());
					key.append(webBodyDTO.tradeCode()).append(webBodyDTO.subTradeCode());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebBodyDTO 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addMapperFactoryBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				MapperFactory mapperFactory = v.getAnnotation(MapperFactory.class);
				Optional.ofNullable(mapperFactory).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.MAPPER_FACTORY.getName());
					key.append(mapperFactory.value());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation MapperFactory 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebHeaderParamBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebHeaderParam webHeaderParam = v.getAnnotation(WebHeaderParam.class);
				Optional.ofNullable(webHeaderParam).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.HEADER_PARAM.getName());
					key.append(webHeaderParam.value());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebHeaderParam 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebBodyParamBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebBodyParam webBodyParam = v.getAnnotation(WebBodyParam.class);
				Optional.ofNullable(webBodyParam).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.BODY_PARAM.getName());
					key.append(webBodyParam.tradeCode()).append(webBodyParam.subTradeCode());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebBodyParam 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}

	public void addWebHeaderVOBean(Set<Class<?>> anntationSet) {
		anntationSet.forEach(v -> {
			try {
				WebHeaderVO webHeaderVO = v.getAnnotation(WebHeaderVO.class);
				Optional.ofNullable(webHeaderVO).ifPresent(dto -> {
					StringBuilder key = new StringBuilder(WebRouteParamsEnums.HEADER_VO.getName());
					key.append(webHeaderVO.value());
					DomainApplicationContainer.build().add(key.toString(), v);
				});
			} catch (Exception e) {
				LOGGER.error("@annotation WebHeaderParam 注解 class 注册容器异常，exception：{}", e);
			}
		});
	}
}
