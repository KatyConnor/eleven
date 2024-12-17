package hx.nine.eleven.httpclient.utils;

import hx.nine.eleven.commons.utils.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * xml处理
 *
 * @author wml
 * 2020-06-05
 */
public class XmlUtils {

    // 多线程安全的Context.
    private JAXBContext jaxbContext;

    public static XmlUtils builder(Class<?>... types) {
        try {
            return new XmlUtils(types);
        } catch (Exception e) {
            // 失败
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 所有需要序列化的Root对象的类型.
     *
     * @param types
     * @throws Exception
     */
    public XmlUtils(Class<?>... types) throws Exception {
        jaxbContext = JAXBContext.newInstance(types);
    }

    /**
     * object 对象转为xml
     *
     * @param root     被转换对象
     * @param encoding 字符集
     * @return
     * @throws Exception
     */
    public String objToXml(Object root, String encoding) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(encoding).marshal(root, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    /**
     * 集合collection转换为xml字符串, 特别支持对Root Element是Collection的情形.
     *
     * @param root
     * @param rootName
     * @param encoding
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String objToXml(Collection root, String rootName, String encoding) {
        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.collection = root;
        JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
                new QName(rootName), CollectionWrapper.class, wrapper);
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(encoding).marshal(wrapperElement, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    /**
     * 解析xml（忽略命名空间），将xml解析成需要的Object对象
     * Xml->Java Object.
     */
    public <T> JAXBElement<T> xmlToObj(String xml, Class<T> tClass) throws Exception {
        StringReader reader = new StringReader(xml);
        SAXParserFactory sax = SAXParserFactory.newInstance();
        sax.setNamespaceAware(false);
        XMLReader xmlReader = sax.newSAXParser().getXMLReader();
        Source source = new SAXSource(xmlReader, new InputSource(reader));
        Unmarshaller marshaller = jaxbContext.createUnmarshaller();
        return marshaller.unmarshal(source, tClass);
    }

    /**
     * 创建Marshaller, 设定encoding(可为Null).
     */
    public Marshaller createMarshaller(String encoding) {
        Marshaller marshaller = null;
        try {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
        } catch (Exception e) {
            // 失败
        }
        return marshaller;
    }

    /**
     * 创建UnMarshaller.
     */
    public Unmarshaller createUnmarshaller() throws Exception {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller;
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {
        @SuppressWarnings("unchecked")
        @XmlAnyElement
        protected Collection collection;
    }
}
