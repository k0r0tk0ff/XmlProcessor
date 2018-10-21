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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class XmlParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);

    public Set<RawEntry> getDataFromXmlFile(String fileName) throws XmlParserException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
        } catch (ParserConfigurationException e) {
            throw new XmlParserException("Parser error!",e);
        } catch (SAXException e) {
            throw new XmlParserException("SAX error!",e);
        } catch (IOException e) {
            throw new XmlParserException("Read xml file error!",e);
        }

        if (document.getDocumentElement() != null) {
            document.getDocumentElement().normalize();
        }

        NodeList nodeList = document.getElementsByTagName("entry");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("See data from loaded xml: ");
        }
        return convertXmlDataToSet(nodeList);
    }

    private Set<RawEntry> convertXmlDataToSet(NodeList nodeList) throws XmlParserException {
        Set<RawEntry> dataFromXmlFile = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                RawEntry entry = convertNodeToEntry(node);
                EntryChecker.checkExistEntryInSet(dataFromXmlFile, entry);
                dataFromXmlFile.add(entry);
            }
        }
        return dataFromXmlFile;
    }

    private RawEntry convertNodeToEntry(Node node){
        Element eElement = (Element) node;
        RawEntry entry = new RawEntry();
        entry.setDepCode(eElement.getAttribute("depcode"));
        entry.setDepJob(eElement.getAttribute("depjob"));
        entry.setDescription(eElement.getAttribute("description"));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("");
            LOGGER.debug("depcode : " + entry.getDepCode());
            LOGGER.debug("depjob : " + entry.getDepJob());
            LOGGER.debug("description : " + entry.getDescription());
        }
        return entry;
    }
}


