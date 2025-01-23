package com.maxipago.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.maxipago.Environment;

@XmlSeeAlso({TransactionRequest.class, ApiRequest.class, RApiRequest.class})
public abstract class AbstractRequest<A, B> {
    @XmlElement(name = "verification")
    public Environment verification;
    private HashMap<String, String> headers = new HashMap<>();
    public static String encode = StandardCharsets.UTF_8.toString();
    
    public AbstractRequest() {
    }

    public AbstractRequest(Environment verification) {
        this.verification = verification;
    }

    protected abstract Class<A> getContext();

    protected abstract B getResponseObject();

    protected void specialCase(B response, Field field, Document document) {
    }

    private void log(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            
            tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            Logger.getLogger("SDK-Java").info(sw.toString());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    protected B parse(InputStream inputStream) {
        B response = getResponseObject();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Field[] fields = response.getClass().getDeclaredFields();

            log(document);

            for (Field field : fields) {
                try {
                    if (!field.getType().getName().equals("java.lang.String")) {
                        specialCase(response, field, document);
                        continue;
                    }

                    field.setAccessible(true);
                    field.set(response, getTextContent(document, field.getName()));
                } catch (IllegalAccessException e) {
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return response;
    }

    public B execute() {
        StringWriter writer = new StringWriter();

        try {
            JAXBContext requestContext = JAXBContext.newInstance(getContext());

            Marshaller marshaller = requestContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encode);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(this, new StreamResult(writer));

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(verification.getEndpoint());

            final RequestConfig params = RequestConfig
                    .custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .build();

            httpPost.setEntity(new StringEntity(writer.toString(), encode));
            httpPost.setConfig(params);

            httpPost.addHeader("Content-Type", "text/xml; charset=" + encode);
            httpPost.addHeader("User-Agent", "MaxiPago-SDK/1.0 (Java; Linux x86_64)");
            for (String key : headers.keySet()){
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(headers.get(key))){
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            HttpResponse response = null;

            response = httpClient.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            InputStream responseEntityContent = null;

            responseEntityContent = responseEntity.getContent();

            Header contentEncoding = response.getFirstHeader("Content-Encoding");

            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                responseEntityContent = new GZIPInputStream(responseEntityContent);
            }

            return parse(responseEntityContent);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String getTextContent(Document document, String elementName) {
        Node element = document.getElementsByTagName(elementName).item(0);

        if (element != null) {
            return element.getTextContent();
        }

        return null;
    }

    public void addHeader(String name, String val) {
        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(val)){
            headers.put(name, val);
        }
    }
}
