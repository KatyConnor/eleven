package hx.nine.eleven.domain.service;

import hx.nine.eleven.domain.response.ResponseEntity;

public class WebServiceFacadeBiz implements WebServiceFacade{

	@Override
	public ResponseEntity doService() {
		// 流程引擎

		// 执行流程引擎
//		DomainMethod
		return null;
	}

	public static WebServiceFacadeBiz build(){
		return Single.INSTANCE;
	}

	private final static class Single{
		private final static WebServiceFacadeBiz INSTANCE = new WebServiceFacadeBiz();
	}
}
