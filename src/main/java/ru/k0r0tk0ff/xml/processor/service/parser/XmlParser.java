package ru.k0r0tk0ff.xml.processor.service.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class XmlParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);

    public static Set<RawEntry> getDataFromXmlFile(String fileName) throws XmlParserException  {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
        } catch (ParserConfigurationException e) {
            LOGGER.error("Parser error!", e);
        } catch (SAXException e) {
            LOGGER.error("SAX error!", e);
        } catch (IOException e) {
            LOGGER.error("Read xml file error!", e);
        }
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("entry");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("See data from loaded xml: ");
        }
        return convertXmlDataToList(nodeList);
    }

    private static Set<RawEntry> convertXmlDataToList(NodeList nodeList) throws XmlParserException {
        Set<RawEntry> dataFromXmlFile = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                RawEntry entry = new RawEntry();
                entry.setDepCode(eElement.getAttribute("depcode"));
                entry.setDepJob(eElement.getAttribute("depjob"));
                entry.setDescription(eElement.getAttribute("description"));

                validateEntry(dataFromXmlFile, entry);
                dataFromXmlFile.add(entry);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("");
                    LOGGER.debug("depcode : " + eElement.getAttribute("depcode"));
                    LOGGER.debug("depjob : " + eElement.getAttribute("depjob"));
                    LOGGER.debug("description : " + eElement.getAttribute("description"));
                }
            }
        }
        return dataFromXmlFile;
    }

    private static void validateEntry(Set<RawEntry> dataFromXmlFile, RawEntry entry) throws XmlParserException {
        if (dataFromXmlFile.contains(entry)) {
            LOGGER.error("Find dublicate in XML file. Please, fix this problem.");
            LOGGER.error("duplicate depcode : " + entry.getDepCode());
            LOGGER.error("duplicate depjob : " + entry.getDepJob());
            throw new XmlParserException();
        }
    }
}


