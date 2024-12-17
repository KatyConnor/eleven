package hx.nine.eleven.httpclient.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author wml
 * 2020-06-05
 */
public class SoapIOHandler {

    private static final Logger logger = LoggerFactory.getLogger(SoapIOHandler.class);

    public SoapIOHandler() {
    }

    public static SOAPMessage formatSoapString(String soapString) throws SOAPException, IOException {
        InputStream is = new ByteArrayInputStream(soapString.getBytes());
        return formatSoapString((InputStream)is);
    }

    public static SOAPMessage formatSoapString(InputStream is) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(), is);
        message.saveChanges();
        return message;
    }

    public static SOAPElement getChildSoapElementByName(SOAPMessage message, String elemName) throws SOAPException {
        if (elemName == null) {
            return null;
        } else {
            SOAPBody body = message.getSOAPBody();
            Iterator<Object> it = body.getChildElements();
            SOAPElement reqElem = null;

            while(it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof SOAPElement) {
                    SOAPElement soapElem = (SOAPElement)obj;
                    if (elemName.equals(soapElem.getElementName().getLocalName())) {
                        reqElem = soapElem;
                        break;
                    }
                }
            }

            return reqElem;
        }
    }

    public static Map element2Map(SOAPElement elem, Map<String, Object> map) {
        if (elem == null) {
            return null;
        } else {
            String key = elem.getElementName().getLocalName();
            if (isSOAPElementTextOnly(elem)) {
                map.put(key, elem.getTextContent());
            } else if (elem.getChildNodes().getLength() == 0) {
                map.put(key, (Object)null);
            } else {
                Map<String, Object> subMap = new HashMap();
                Iterator it = elem.getChildElements();

                while(it.hasNext()) {
                    Object obj = it.next();
                    if (obj instanceof SOAPElement) {
                        SOAPElement subElem = (SOAPElement)obj;
                        element2Map(subElem, subMap);
                    }
                }

                boolean isFirst = !map.containsKey(key);
                if (isFirst) {
                    map.put(key, subMap);
                } else {
                    Object val = map.get(key);
                    if (val instanceof List) {
                        ((List)val).add(subMap);
                    } else {
                        List<Object> listSub = new ArrayList();
                        listSub.add(val);
                        listSub.add(subMap);
                        map.put(key, listSub);
                    }
                }
            }

            return map;
        }
    }

    private static boolean isSOAPElementTextOnly(SOAPElement elem) {
        if (elem.getChildNodes().getLength() == 1) {
            Node n = elem.getFirstChild();
            if (!n.hasChildNodes() && "com.sun.xml.internal.messaging.saaj.soap.impl.TextImpl".equals(n.getClass().getName())) {
                return true;
            }
        }

        return false;
    }

    public static Map getFaultMap(SOAPMessage message) throws SOAPException {
        SOAPBody body = message.getSOAPBody();
        SOAPFault saopFault = body.getFault();
        Map<String, Object> faultMap = new HashMap();
        faultMap.put("faultcode", saopFault.getFaultCode());
        faultMap.put("faultstring", saopFault.getFaultString());
        SOAPFactory soapfactory = SOAPFactory.newInstance();
        Iterator it = saopFault.getDetail().getChildElements();
        SOAPElement txStatusElem = null;

        while(it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof SOAPElement) {
                SOAPElement soapElem = (SOAPElement)obj;
                if ("TxStatus".equals(soapElem.getElementName().getLocalName())) {
                    txStatusElem = soapElem;
                    break;
                }
            }
        }

        faultMap.put("TxStatus", txStatusElem.getTextContent());
        return faultMap;
    }

    public static SOAPElement packSoapRequest(Map<String, Object> soapBodyMap) throws SOAPException {
        SOAPFactory soapfactory = SOAPFactory.newInstance();
        Name respName = soapfactory.createName("Request", (String)null, (String)null);
        SOAPElement elem = map2SOAPElement(soapBodyMap, respName);
        return elem;
    }

    public static SOAPMessage buildSoapMessage(Map<String, String> faultMap, SOAPElement bodyElem) throws SOAPException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        message.getMimeHeaders().setHeader("SOAPAction", "http://schemas.xmlsoap.org/soap/envelope/");
        SOAPBody body = message.getSOAPBody();
        if (bodyElem != null) {
            body.addChildElement(bodyElem);
        }

        if (faultMap != null) {
            SOAPFault fault = body.addFault();
            fault.setFaultCode(String.format("%s", faultMap.get("faultcode")));
            fault.setFaultString(String.format("%s", faultMap.get("faultstring")));
            Detail detail = fault.addDetail();
            SOAPElement txStatusElem = detail.addChildElement("TxStatus");
            if ("AAAAAAA".equals(faultMap.get("faultcode"))) {
                txStatusElem.setTextContent("SUCC");
            } else {
                txStatusElem.setTextContent("FAIL");
            }
        }

        return message;
    }

    public static String toString(SOAPMessage message) {
        String result = null;
        if (message != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                message.writeTo(baos);
                result = baos.toString();
            } catch (IOException | SOAPException var12) {
                result = var12.getMessage();
                logger.error("SOAPMessage toString Exception: " + var12.getMessage());
            } finally {
                try {
                    baos.close();
                } catch (IOException var11) {
                    logger.error("close ByteArrayOutputStream error");
                }

            }
        }

        return result;
    }

    public static ByteArrayOutputStream toOutputStream(SOAPMessage message) {
        String result = null;
        if (message != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ByteArrayOutputStream var3;
            try {
                message.writeTo(baos);
                var3 = baos;
            } catch (IOException | SOAPException var13) {
                result = var13.getMessage();
                logger.error("SOAPMessage toString Exception: " + var13.getMessage());
                return null;
            } finally {
                try {
                    baos.close();
                } catch (IOException var12) {
                    logger.error("close ByteArrayOutputStream error");
                }

            }

            return var3;
        } else {
            return null;
        }
    }

    private static Object obj2SOAPElement(Object obj, Name elemName) throws SOAPException {
        SOAPFactory soapfactory = SOAPFactory.newInstance();
        SOAPElement elem = null;
        if (obj != null) {
            if (obj instanceof Map) {
                Map<String, Object> subContext = (Map)obj;
                elem = map2SOAPElement(subContext, elemName);
                return elem;
            } else if (obj instanceof List) {
                List objArray = (List)obj;
                List<SOAPElement> resultArray = new ArrayList();
                Iterator var6 = objArray.iterator();

                while(var6.hasNext()) {
                    Object val = var6.next();
                    Object subObj = obj2SOAPElement(val, elemName);
                    if (subObj instanceof SOAPElement) {
                        SOAPElement subelem = (SOAPElement)subObj;
                        resultArray.add(subelem);
                    } else {
                        List<SOAPElement> subResultArray = (List)subObj;
                        resultArray.addAll(subResultArray);
                    }
                }

                return resultArray;
            } else {
                elem = soapfactory.createElement(elemName);
                elem.setValue(obj.toString());
                return elem;
            }
        } else {
            return elem;
        }
    }

    public static SOAPElement map2SOAPElement(Map<String, Object> context, Name elemName) throws SOAPException {
        SOAPFactory soapfactory = SOAPFactory.newInstance();
        SOAPElement elem = soapfactory.createElement(elemName);
        Set<String> keySet = context.keySet();
        Iterator var5 = keySet.iterator();

        while(true) {
            while(true) {
                while(var5.hasNext()) {
                    String key = (String)var5.next();
                    Object value = context.get(key);
                    if (value != null) {
                        Name subElemName = soapfactory.createName(key);
                        Object soapElementOrList = obj2SOAPElement(value, subElemName);
                        if (soapElementOrList instanceof SOAPElement) {
                            SOAPElement subElem = (SOAPElement)soapElementOrList;
                            elem.addChildElement(subElem);
                        } else if (soapElementOrList instanceof List) {
                            List<SOAPElement> subElemList = (List)soapElementOrList;
                            Iterator var11 = subElemList.iterator();

                            while(var11.hasNext()) {
                                SOAPElement subElem = (SOAPElement)var11.next();
                                elem.addChildElement(subElem);
                            }
                        }
                    } else {
                        logger.debug("key:" + key + " is null,skip build this node");
                    }
                }

                return elem;
            }
        }
    }
}
