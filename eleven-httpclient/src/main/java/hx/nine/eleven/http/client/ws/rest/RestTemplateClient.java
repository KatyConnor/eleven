package hx.nine.eleven.http.client.ws.rest;


import hx.nine.eleven.http.client.utils.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.util.CharsetUtil;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author wml
 * 2020-06-08
 */
public class RestTemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateClient.class);
    Charset charset;
    private RestTemplate restTemplate;
    private String host;
    private int port;
    private String preUrl;

    public RestTemplateClient() {
        this.charset = CharsetUtil.UTF_8;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void initUrl() {
        this.preUrl = "http://" + this.host + ":" + this.port;
    }

    public Map sendAndReciveMultiPart(URI uri, Map<String, Object> reqMap, Map<String, FileSystemResource> reqFileMap) {
        String url = this.preUrl + uri.toString();
        logger.debug("url:" + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> reqParams = new LinkedMultiValueMap();
        Iterator var7;
        String key;
        if (reqFileMap != null && reqFileMap.size() > 0) {
            var7 = reqFileMap.keySet().iterator();

            while(var7.hasNext()) {
                key = (String)var7.next();
                reqParams.add(key, reqFileMap.get(key));
            }
        }

        if (reqMap != null && reqMap.size() > 0) {
            var7 = reqMap.keySet().iterator();

            while(var7.hasNext()) {
                key = (String)var7.next();
                reqParams.add(key, reqMap.get(key));
            }
        }

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(reqParams, headers);
        logger.debug("send:" + httpEntity);
        key = (String)this.restTemplate.postForObject(url, httpEntity, String.class, new Object[0]);
        logger.debug("recv:" + key);
        return JSONObject.parseObjectToMap(key);
    }

    public Map sendAndRecive(URI uri, Map<String, Object> reqMap) {
        String reqString;
        if (reqMap != null && reqMap.size() != 0) {
            reqString = JSONObject.toJSONString(reqMap);
        } else {
            reqString = null;
        }

        String respString = this.sendAndRecive(uri, reqString);
        return JSONObject.parseObjectToMap(respString);
    }

    private String sendAndRecive(URI uri, String reqJsonMsg) {
        String url = this.preUrl + uri.toString();
        logger.debug("url:" + url);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);
        HttpEntity httpEntity = HttpEntity.EMPTY;
        if (reqJsonMsg != null && reqJsonMsg.length() > 0) {
            httpEntity = new HttpEntity(reqJsonMsg, headers);
        }

        logger.debug("send:" + httpEntity);
        String respString = (String)this.restTemplate.postForObject(url, httpEntity, String.class, new Object[0]);
        logger.debug("recv:" + respString);
        return respString;
    }

    public String doPost(String url, Map<String, String> formParams) {
        String response = null;
        if (formParams != null && formParams.size() != 0) {
            try {
                ObjectNode requestJSON = (new ObjectMapper()).createObjectNode();
                formParams.keySet().stream().forEach((key) -> {
                    requestJSON.put(key, (String)formParams.get(key));
                });
                HttpEntity<String> httpEntity = new HttpEntity(requestJSON.toString());
                response = (String)this.restTemplate.postForObject(url, httpEntity, String.class, new Object[0]);
                return new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception var6) {
                logger.error("POST请求出错：{}", url, var6);
                return null;
            }
        } else {
            return this.doPost(url);
        }
    }

    public String doPost(String url) {
        try {
            return (String)this.restTemplate.postForObject(url, HttpEntity.EMPTY, String.class, new Object[0]);
        } catch (Exception var3) {
            logger.error("POST请求出错：{}", url, var3);
            return null;
        }
    }

    public String doGet(String url) {
        try {
            return (String)this.restTemplate.getForObject(url, String.class, new Object[0]);
        } catch (Exception var3) {
            logger.error("GET请求出错：{}", url, var3);
            return null;
        }
    }
}
