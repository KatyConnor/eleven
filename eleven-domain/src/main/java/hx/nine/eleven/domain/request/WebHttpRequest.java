package hx.nine.eleven.domain.request;

import hx.nine.eleven.commons.annotation.FieldTypeConvert;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.domain.conver.WebRequestHeaderFieldConvert;
import hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.domain.conver.BeanConvert;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.domain.obj.form.HeaderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * HTTP 报文传输数据
 *
 * @author wml
 * @date 2022-10-28
 */
public class WebHttpRequest implements Serializable {
	private final static Logger LOGGER = LoggerFactory.getLogger(WebHttpRequest.class);
	/**
	 * 请求报文头（header）
	 */
	@NotNull(message = "请求报文头 [header] 不能为空")
	@FieldTypeConvert(using = WebRequestHeaderFieldConvert.class)
	private HeaderForm requestHeader;
	/**
	 * 请求报文体[body]
	 */
	private Object requestBody;

	/**
	 * 上传文件列表
	 */
	private List<FileUploadEntity> fileUploadEntities;
	/**
	 * 数据流
	 */
	private InputStream inputStream;

	/**
	 * 是否开启协程处理
	 */
	private Boolean openFiber = false;

	/**
	 * 用户主题数据
	 */
	private Object principal;

	public HeaderForm getRequestHeader() {
		return requestHeader;
	}

	/**
	 * 赋值时，初始化 header 数据，映射到headerForm 头表单对象中，
	 * header 必传数据，包含了必要的交易码
	 *
	 * @param requestHeader
	 */
	public void setRequestHeader(HeaderForm requestHeader) {
		this.requestHeader = requestHeader;
	}

	public Object getRequestBody() {
		return requestBody;
	}

	/**
	 * 赋值时，初始化 body 对象，body数据直接映射成 form表单对象中
	 *
	 * @param requestBody
	 */
	public void setRequestBody(Object requestBody) {
		if (!Optional.ofNullable(requestBody).isPresent()) {
			LOGGER.warn("[{}] is null", requestBody);
			return;
		}
		if(this.requestHeader == null){
			LOGGER.warn("[requestHeader] is null");
			return;
		}
		Object tradeCode = this.requestHeader.getTradeCode();
		Object subTradeCode = this.requestHeader.getSubTradeCode();
		tradeCode = Optional.ofNullable(tradeCode).isPresent() ? tradeCode : "";
		subTradeCode = Optional.ofNullable(subTradeCode).isPresent() ? subTradeCode : "";
		this.requestBody = BeanConvert.convert(requestBody, StringUtils.append(tradeCode.toString(), subTradeCode.toString()),
				null, WebRouteParamsEnums.BODY_FORM.getName());
	}

	public List<FileUploadEntity> getFileUploadEntities() {
		return fileUploadEntities;
	}

	public void setFileUploadEntities(List<FileUploadEntity> fileUploadEntities) {
		this.fileUploadEntities = fileUploadEntities;
		if (!Optional.ofNullable(this.requestBody).isPresent() &&
				Optional.ofNullable(this.fileUploadEntities).isPresent() &&
				this.fileUploadEntities.size() > 0) {
			this.requestBody = Optional.empty();
		}
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Boolean getOpenFiber() {
		return openFiber;
	}

	public void setOpenFiber(Boolean openFiber) {
		this.openFiber = openFiber;
	}

	public Object getPrincipal() {
		return principal;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	@Override
	public String toString() {
		return ObjectUtils.toString(this);
	}
}
