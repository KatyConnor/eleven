package hx.nine.eleven.httpclient.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @param <H>
 * @param <B>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "soapenv:Envelope")
public class RequestSoapenv<H,B> {
    @XmlElement(name = "soapenv:Header")
    private H header;

    @XmlElement(name = "soapenv:Body")
    private B body;

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public B getBody() {
        return body;
    }

    public void setBody(B body) {
        this.body = body;
    }
}
