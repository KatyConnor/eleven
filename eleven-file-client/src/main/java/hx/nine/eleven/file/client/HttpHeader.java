package hx.nine.eleven.file.client;

import org.apache.http.Header;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpHeader {

	private Map<String, String> headers;

	private Header header;

	private Header[] headeres;

	public HttpHeader(){
		this.headers = new HashMap<>();
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Header getHeader() {
		return header;
	}

	public HttpHeader setHeader(Header header) {
		this.header = header;
		return this;
	}

	public Header[] getHeaderes() {
		return headeres;
	}

	public HttpHeader setHeaderes(Header[] headeres) {
		this.headeres = headeres;
		return this;
	}

	public HttpHeader addHeader(String key,String value){
		this.headers.put(key,value);
		return this;
	}

	public Map<String,String> setDfaultHeader(){
		// 模拟浏览器
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("Authorization", "");
		headers.put("Accept", "application/json");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		headers.put("Accept-Charset", "utf-8,ISO-8859-1,gbk,gb2312; q=0.7,*;q=0.7");
		return headers;
	}

	public HttpHeader defaultFileHeader(){
		this.headers.put("SYNC_FILE", getUUID());
		return this;
	}



	public static HttpHeader build(){
		return new HttpHeader();
	}

	private static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String temp = uuid.toString().replaceAll("-", "");
		return temp;
	}
}
