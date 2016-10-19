package org.cresst.sb.irp.itemscoring.client.converter;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import tds.itemscoringengine.ItemScoreRequest;
import tds.itemscoringengine.ItemScoreResponse;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Converts Item Scoring Service requests and responses to ItemScoreRequest and ItemScoreResponse objects, respectively.
 */
public class ItemScoreMessageConverter implements HttpMessageConverter<Object> {
    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        return aClass.equals(ItemScoreResponse.class);
    }

    @Override
    public boolean canWrite(Class aClass, MediaType mediaType) {
        return aClass.equals(ItemScoreRequest.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Lists.newArrayList(MediaType.APPLICATION_XML);
    }

    @Override
    public Object read(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        String xml = CharStreams.toString(new InputStreamReader(httpInputMessage.getBody()));
        try {
            return ItemScoreResponse.getInstanceFromXml(xml);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        ItemScoreRequest itemScoreRequest = (ItemScoreRequest) o;
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(httpOutputMessage.getBody());
            itemScoreRequest.writeXML(xmlStreamWriter);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
