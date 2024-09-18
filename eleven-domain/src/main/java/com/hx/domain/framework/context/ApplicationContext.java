package com.hx.domain.framework.context;

import com.hx.domain.framework.obj.dto.BaseDTO;

import java.util.Optional;

/**
 * 对外API接口工具类
 */
public class ApplicationContext {

	/**
	 * 获取应用上下文
	 * @return
	 */
	public static DomainContext getDomainContext(){
		return DomainContextAware.build().getDomainContext();
	}

	/**
	 * 销毁上下文
	 */
	public static void destroyContext(){
		 DomainContextAware.build().destroyContext();
	}

	public static <T> T getBody(Class<T> bodyClass){
		Object body = DomainContextAware.build().getDomainContext().getRequestBody();
		if (Optional.ofNullable(body).isPresent() && body instanceof BaseDTO
				&& bodyClass.getSimpleName().equals(body.getClass().getSimpleName())){
			return (T)body;
		}
		return null;
	}
}
