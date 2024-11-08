package com.hx.nine.eleven.core.factory.impl;

import com.hx.nine.eleven.commons.utils.JSONObjectMapper;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.exception.ApplicationInitialzerException;
import com.hx.nine.eleven.core.factory.WebRouteHandler;
import com.hx.nine.eleven.core.web.DomainRouter;
import com.hx.nine.eleven.core.web.http.HttpMethodFacade;
import com.hx.nine.eleven.core.web.http.HttpServlet;
import org.reflections.Reflections;
import java.util.Optional;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/11/7
 */
public class HttpServletAndDomainRouterInit implements WebRouteHandler {

	@Override
	public void loadWebRouteHandler(Reflections reflections) throws Throwable {
		DefaultElevenApplicationContext.build().addBean(HttpServlet.class.getName(), new HttpServlet());
		initHttpMethodFacade(reflections);
		initDomainRouter(reflections);


	}

	private void initHttpMethodFacade(Reflections reflections){
		Set<Class<? extends HttpMethodFacade>> httpMethodFacade = reflections.getSubTypesOf(HttpMethodFacade.class);
		Optional.ofNullable(httpMethodFacade).ifPresent(h -> {
			if (h.size() > 1) {
				throw new ApplicationInitialzerException("HttpMethodFacade 实例不唯一,{}", JSONObjectMapper.toJsonString(h));
			}

			if (h.size() == 0){
				throw new ApplicationInitialzerException("没有 HttpMethodFacade 接口实现实例");
			}

			if (h.size() == 1) {
				DefaultElevenApplicationContext.build().
						addBean(HttpMethodFacade.class.getName(), ElevenBeanFactory.createBean(h.stream().findFirst().get()));
			}
		});
	}

	private void initDomainRouter(Reflections reflections){
		Set<Class<? extends DomainRouter>> domainRouter = reflections.getSubTypesOf(DomainRouter.class);
		Optional.ofNullable(domainRouter).ifPresent(d -> {
			if (d.size() > 1) {
				throw new ApplicationInitialzerException("DomainRouter 实例不唯一,{}", JSONObjectMapper.toJsonString(d));
			}

			if (d.size() == 0){
				throw new ApplicationInitialzerException("没有 DomainRouter 接口实现实例");
			}

			if (d.size() == 1) {
				DefaultElevenApplicationContext.build().
						addBean(HttpMethodFacade.class.getName(), ElevenBeanFactory.createBean(d.stream().findFirst().get()));
			}
		});
	}
}
