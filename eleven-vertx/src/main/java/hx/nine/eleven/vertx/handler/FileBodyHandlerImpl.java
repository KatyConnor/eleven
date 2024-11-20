package hx.nine.eleven.vertx.handler;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.enums.ContentTypeEnums;
import hx.nine.eleven.core.utils.MDCThreadUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;

/**
 * 判断是否文件上床处理类或者MULTIPART_FORM_DATA表单处理
 */
public class FileBodyHandlerImpl extends BodyHandlerImpl {

	public static BodyHandler createBodyHandler() {
		return new FileBodyHandlerImpl();
	}

	@Override
	public void handle(RoutingContext context) {
		MDCThreadUtil.wrap();
		final boolean isMultipart;
		final boolean isUrlEncoded;
		String contentType = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
		// 如果不带contentType，默认走APPLICATION_JSON
		if (StringUtils.isEmpty(contentType)){
			contentType = ContentTypeEnums.APPLICATION_JSON.getCode();
		}
		String lowerCaseContentType = contentType.toLowerCase();
		isMultipart = lowerCaseContentType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString());
		isUrlEncoded = lowerCaseContentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString());
		if (isMultipart || isUrlEncoded) {
			super.handle(context);
		}else {
			HttpServerRequest httpServerRequest = context.request();
			long parsedContentLength = this.parseContentLengthOfHeader(httpServerRequest);
			BodyBufferHandler handler = new BodyBufferHandler(context,parsedContentLength);
			boolean ended = httpServerRequest.isEnded();
			if (!ended) {
				HttpServerRequest request = httpServerRequest.handler(handler);
				request.endHandler(handler::end).resume();
			}
		}
	}

	private long parseContentLengthOfHeader(HttpServerRequest request) {
		String contentLength = request.getHeader(HttpHeaders.CONTENT_LENGTH);
		if (contentLength != null && !contentLength.isEmpty()) {
			try {
				long parsedContentLength = Long.parseLong(contentLength);
				return parsedContentLength < 0L ? -1L : parsedContentLength;
			} catch (NumberFormatException var5) {
				return -1L;
			}
		} else {
			return -1L;
		}
	}
}
