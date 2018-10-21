package ru.k0r0tk0ff.xml.processor.service.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;

import java.util.Set;

/**
 * Created by korotkov_a_a on 21.10.2018.
 */
public class EntryChecker {
    public static void checkExistEntryInSet(Set<RawEntry> dataFromXmlFile, RawEntry entry) throws XmlParserException {
        if (dataFromXmlFile.contains(entry)) {
            throw new XmlParserException("Find duplicate in XML file. duplicate depcode: " + entry.getDepCode() + " duplicate depjob: " + entry.getDepJob());
        }
    }
}
