package ru.k0r0tk0ff.xml.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DaoException;
import ru.k0r0tk0ff.xml.processor.dao.H2DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParser;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParserException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by korotkov_a_a on 20.10.2018.
 */
public class Synchronizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Synchronizer.class);

    private final String fileName;
    private final H2DbDao h2DbDao;

    public Synchronizer(H2DbDao h2DbDao, String fileName) {
        this.fileName = fileName;
        this.h2DbDao = h2DbDao;
    }

    private Set<RawEntry> entriesFromFile;
    private Set<RawEntry> entriesFromDb;


    public void synchronize() throws XmlParserException, DaoException {
        initializeDataSets();
        synchronizeData(entriesFromFile, entriesFromDb);
        LOGGER.info("Synchronize with file success.");
    }

    private void initializeDataSets() throws XmlParserException, DaoException {
        XmlParser xmlParser = new XmlParser();
        entriesFromFile = xmlParser.getDataFromXmlFile(fileName);
        DataConverter dataConverter = new DataConverter(h2DbDao);
        entriesFromDb = dataConverter.convertDbDataToRawEntries();
    }

    private void synchronizeData(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb) throws DaoException {
        Set<RawEntry> entriesForDeleteInDb = getEntriesForDeleteInDb(entriesFromFile, entriesFromDb);
        if(!entriesForDeleteInDb.isEmpty()) {
            h2DbDao.deleteMissingEntriesInDb(entriesForDeleteInDb);
        }

        Set<RawEntry> entriesForInsertToDb = getEntriesForInsertToDb(entriesFromFile, entriesFromDb);
        if(!entriesForInsertToDb.isEmpty()) {
            h2DbDao.insertEntriesToDb(entriesForInsertToDb);
        }

        Set<RawEntry> entriesForUpdateInDb = getEntriesForUpdateInDb(entriesFromFile, entriesFromDb);
        if(!entriesForUpdateInDb.isEmpty()) {
            h2DbDao.updateEntriesInDb(entriesForUpdateInDb);
        }
    }

    private Set<RawEntry> getEntriesForUpdateInDb(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb) {
        Set<RawEntry> entriesForUpdateInDb;
        entriesForUpdateInDb = findEntriesForUpdateInDb(entriesFromFile, entriesFromDb);
        if (LOGGER.isDebugEnabled()) {
            showEntriesInDebugMode(entriesForUpdateInDb, "update");
        }
        return entriesForUpdateInDb;
    }

    private Set<RawEntry> getEntriesForInsertToDb(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb) {
        Set<RawEntry> entriesForInsertToDb;
        entriesForInsertToDb = findUniqueEntriesFromSetXinAnotherSetY(entriesFromFile, entriesFromDb);
        if (LOGGER.isDebugEnabled()) {
            showEntriesInDebugMode(entriesForInsertToDb, "insert");
        }
        return entriesForInsertToDb;
    }

    private Set<RawEntry> getEntriesForDeleteInDb(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb) {
        Set<RawEntry> entriesForDeleteInDb;
        entriesForDeleteInDb = findUniqueEntriesFromSetXinAnotherSetY(entriesFromDb, entriesFromFile);
        if (LOGGER.isDebugEnabled()) {
            showEntriesInDebugMode(entriesForDeleteInDb, "delete");
        }
        return entriesForDeleteInDb;
    }

    private void showEntriesInDebugMode(Set<RawEntry> entries, String operation) {
        if (!entries.isEmpty()) {
            LOGGER.debug("DepCode of Entries for " + operation);
            Consumer<RawEntry> showRawEntryInLog = (x) -> LOGGER.debug(x.getDepCode());
            entries.forEach(showRawEntryInLog);
        }
    }

    private Set<RawEntry> findUniqueEntriesFromSetXinAnotherSetY(Set<RawEntry> setX, Set<RawEntry> setY){
        Set<RawEntry> result;
        result = setX
                .stream()
                .filter(x -> !setY.contains(x))
                .collect(Collectors.toSet());
        return result;
    }

    private Set<RawEntry> findEntriesForUpdateInDb(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb){
        Set<RawEntry> result = new HashSet<>();

        Map<Integer, RawEntry> theSameEntriesFromFile =
                entriesFromFile
                .stream()
                .filter(entriesFromDb::contains)
                .collect(Collectors.toMap(
                RawEntry::hashCode, x -> x
                ));

        Map<Integer, RawEntry> theSameEntriesFromDb =
                entriesFromDb
               .stream()
               .filter(entriesFromFile::contains)
               .collect(Collectors.toMap(
                       RawEntry::hashCode, x -> x
               ));

        for (Map.Entry<Integer, RawEntry> entry: theSameEntriesFromFile.entrySet()) {
            Integer hashCode = entry.getKey();
            RawEntry entryFromX = entry.getValue();
            String descrInSetX = entryFromX.getDescription();
            String descrInSetY = theSameEntriesFromDb.get(hashCode).getDescription();
            if(!descrInSetX.equals(descrInSetY)) {
                result.add(entryFromX);
            }
        }
        return result;
    }
}
