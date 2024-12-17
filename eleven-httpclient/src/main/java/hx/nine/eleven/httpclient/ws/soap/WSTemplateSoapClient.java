package hx.nine.eleven.httpclient.ws.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import hx.nine.eleven.httpclient.utils.SoapIOHandler;
import hx.nine.eleven.httpclient.utils.XmlUtils;
import hx.nine.eleven.httpclient.xml.RequestSoapenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wml
 * 2020-06-08
 */
public class WSTemplateSoapClient {

    private static final Logger logger = LoggerFactory.getLogger(WSTemplateSoapClient.class);

    public WSTemplateSoapClient() {
    }

    public static OutputStream sendAndRecieve(String uri, Map<String, Object> subReqMap, int readTimeout) throws SOAPException, IOException {
        WebServiceTemplate wsTemplate = new WebServiceTemplate();
        RequestSoapenv reqMap = new RequestSoapenv();
        reqMap.setBody(subReqMap);
        String reqString = XmlUtils.builder(RequestSoapenv.class).objToXml(reqMap,"UTF-8");
        Source requestPayload = new StringSource(reqString);
        OutputStream os = new ByteArrayOutputStream();
        Result responseResult = new StreamResult(os);
        logger.info("send:" + requestPayload);

        wsTemplate.sendSourceAndReceiveToResult(uri, requestPayload, responseResult);

        logger.info("recv:" + os);
        SOAPMessage respMessage = SoapIOHandler.formatSoapString(os.toString());
        return os;
    }

    public static void main(String[] args) {
        String uri = "http://10.181.68.232:20022/00200199000test";
        Map<String, Object> reqMap = new HashMap();
        reqMap.put("RequestHeader", (Object)null);
        Map<String, Object> reqBodyMap = new HashMap();
        reqBodyMap.put("TxDate", "20180716");
        Map<String, Object> recMsgMap1 = new HashMap();
        recMsgMap1.put("MntOper", "200131");
        Map<String, Object> recMsgMap2 = new HashMap();
        recMsgMap2.put("MntOper", "200132");
        List recMsgList = new ArrayList();
        recMsgList.add(recMsgMap1);
        recMsgList.add(recMsgMap2);
        reqBodyMap.put("RecMsg", recMsgList);
        reqMap.put("RequestBody", reqBodyMap);

        try {
            OutputStream os = sendAndRecieve(uri, reqMap, 0);
            System.out.println(os);
        } catch (SOAPException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }
}
