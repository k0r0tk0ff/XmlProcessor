package ru.k0r0tk0ff.xml.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParser;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParserException;

import java.util.HashMap;
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
    private final DbDao dbDao;

    public Synchronizer(DbDao dbDao, String fileName) {
        this.fileName = fileName;
        this.dbDao = dbDao;
    }

    private Set<RawEntry> entriesFromFile;
    private Set<RawEntry> entriesFromDb;


    public void synchronize() throws XmlParserException {
        initializeDataSets();
        synchronizeData(entriesFromFile, entriesFromDb);
        LOGGER.info("Synchronize with file success.");
    }

    private void initializeDataSets() throws XmlParserException {
        entriesFromFile = XmlParser.getDataFromXmlFile(fileName);
        DbDataGetter dbDataGetter = new DbDataGetter(dbDao);
        entriesFromDb = dbDataGetter.getDataFromDb();
    }

    private void synchronizeData(Set<RawEntry> entriesFromFile, Set<RawEntry> entriesFromDb) {
        Set<RawEntry> entriesForDeleteInDb = getEntriesForDeleteInDb(entriesFromFile, entriesFromDb);
        if(!entriesForDeleteInDb.isEmpty()) {
            dbDao.deleteMissingEntriesInDb(entriesForDeleteInDb);
        }

        Set<RawEntry> entriesForInsertToDb = getEntriesForInsertToDb(entriesFromFile, entriesFromDb);
        if(!entriesForInsertToDb.isEmpty()) {
            dbDao.insertEntriesToDb(entriesForInsertToDb);
        }

        Set<RawEntry> entriesForUpdateInDb = getEntriesForUpdateInDb(entriesFromFile, entriesFromDb);
        if(!entriesForUpdateInDb.isEmpty()) {
            dbDao.updateEntriesInDb(entriesForUpdateInDb);
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

    private Set<RawEntry> findEntriesForUpdateInDb(Set<RawEntry> setX, Set<RawEntry> setY){
        Set<RawEntry> result = new HashSet<>();

        Map<Integer, RawEntry> mapInX =
                setX
                .stream()
                .filter(setY::contains)
                .collect(Collectors.toMap(
                RawEntry::hashCode, x -> x
                ));

        Map<Integer, RawEntry> mapInY =
                setY
               .stream()
               .filter(setX::contains)
               .collect(Collectors.toMap(
                       RawEntry::hashCode, x -> x
               ));

        for (Map.Entry<Integer, RawEntry> entry: mapInX.entrySet()) {
            Integer hashCode = entry.getKey();
            RawEntry entryFromX = entry.getValue();
            String descrInSetX = entryFromX.getDescription();
            String descrInSetY = mapInY.get(hashCode).getDescription();
            if(!descrInSetX.equals(descrInSetY)) {
                result.add(entryFromX);
            }
        }
        return result;
    }
}
