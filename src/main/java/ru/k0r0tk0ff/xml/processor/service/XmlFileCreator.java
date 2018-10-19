package ru.k0r0tk0ff.xml.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Set;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */
public class XmlFileCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFileCreator.class);

    private Set<RawEntry> entries;
    private String fileName;

    public XmlFileCreator(Set<RawEntry> entries, String fileName) {
        this.entries = entries;
        this.fileName = fileName;
    }

    public void convertDataToXmlAndSave() throws XMLStreamException, FileNotFoundException, TransformerException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        //XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(tmpFileName));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        XMLStreamWriter writer = factory.createXMLStreamWriter(buffer);

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("data");
        for (RawEntry rawEntry : entries) {
            writer.writeStartElement("entry");
            writer.writeAttribute("depcode", rawEntry.getDepCode());
            writer.writeAttribute("depjob", rawEntry.getDepJob());
            writer.writeAttribute("description", rawEntry.getDescription());
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(
                new StreamSource(new ByteArrayInputStream(buffer.toByteArray())),
                new StreamResult(new FileOutputStream(fileName)
        ));

        /*transformer.transform(new StreamSource(
                        new BufferedInputStream(new FileInputStream(tmpFileName))),
                new StreamResult(new FileOutputStream(filename))
        );*/
    }
}
