package com.hx.http.client.ws.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import com.hx.http.client.utils.SoapIOHandler;
import com.hx.http.client.ws.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wml
 * 2020-06-08
 */
public class SoapClient {

    private static final Logger logger = LoggerFactory.getLogger(SoapClient.class);
    private String charsetName;
    private HttpClient httpclient;

    public SoapClient() {
    }

    public HttpClient getHttpclient() {
        return this.httpclient;
    }

    public void setHttpclient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public String getCharsetName() {
        return this.charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String sendAndRecive(URI uri, String reqMsg) throws UnsupportedEncodingException, InterruptedException, URISyntaxException {
        String reqString = String.format("<?xml version=\"1.0\" encoding=\"%s\"?>", this.charsetName) + reqMsg;
        byte[] httpContentBytes = reqString.getBytes(this.charsetName);
        logger.info("send:" + reqString);
        String respMsg = this.httpclient.sendAndRecive(uri, httpContentBytes);
        logger.debug("recv:{}", respMsg);
        return respMsg;
    }

    public String sendAndRecive(URI uri, ByteArrayOutputStream soapEnvelopeBaos) throws UnsupportedEncodingException, InterruptedException, URISyntaxException {
        byte[] httpContentBytes = soapEnvelopeBaos.toByteArray();
        logger.debug("send:{}", new String(httpContentBytes, this.charsetName));
        String respMsg = this.httpclient.sendAndRecive(uri, httpContentBytes);
        logger.debug("recv:{}", respMsg);
        return respMsg;
    }

    public Map sendAndRecive(URI uri, Map<String, Object> subReqMap) {
        Map resultMap = new HashMap();
        ByteArrayOutputStream baos = null;
        Properties p = System.getProperties();
        logger.info("Properties:{}", p);

        SOAPMessage respMessage;
        try {
            logger.debug("subReqMap:{}", subReqMap);
            SOAPElement reqElement = SoapIOHandler.packSoapRequest(subReqMap);
            logger.debug("reqElement:{}" + reqElement);
            respMessage = SoapIOHandler.buildSoapMessage((Map)null, reqElement);
            respMessage.setProperty("javax.xml.soap.character-set-encoding", this.charsetName);
            respMessage.setProperty("javax.xml.soap.write-xml-declaration", "true");
            baos = SoapIOHandler.toOutputStream(respMessage);
            logger.debug("DefaultCharset:" + Charset.defaultCharset());
            logger.debug("soap packet:{}", baos.toString());
        } catch (SOAPException var14) {
            logger.error("组装请求报文出错", var14);
            return null;
        }

        String respMsg = null;

        try {
            respMsg = this.sendAndRecive(uri, baos);
        } catch (InterruptedException | URISyntaxException | UnsupportedEncodingException var13) {
            logger.error("发送并接收报文出错", var13);
        }

        try {
            respMessage = SoapIOHandler.formatSoapString(respMsg);
            Map faultMap = new HashMap();
            SOAPFault soapFault = respMessage.getSOAPBody().getFault();
            SoapIOHandler.element2Map(soapFault, faultMap);
            resultMap.putAll(faultMap);
            logger.info("faultMap:" + faultMap);
            SOAPElement respElem = SoapIOHandler.getChildSoapElementByName(respMessage, "Response");
            Map respMap = new HashMap();
            SoapIOHandler.element2Map(respElem, respMap);
            resultMap.putAll(respMap);
            logger.info("response Map:" + respMap);
            logger.info("response result Map:" + resultMap);
        } catch (IOException | SOAPException var12) {
            logger.error("解析返回报文出错" + var12.getMessage() + "  " + var12);
        }

        return resultMap;
    }
}
