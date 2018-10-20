package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.infrastructure.AppPropertiesHolder;
import ru.k0r0tk0ff.xml.processor.service.DbDataGetter;
import ru.k0r0tk0ff.xml.processor.service.Synchronizer;
import ru.k0r0tk0ff.xml.processor.service.XmlFileCreator;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParserException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */
public class Process {
    private final static String LOG_FILE_NAME_KEY = "xml.processor.log.filename";
    private final static String LOG_LEVEL_KEY = "xml.processor.log.level";
    private static Logger LOGGER;

    private DbDao dbDao;

    public void setDbDao(DbDao dbDao) {
        this.dbDao = dbDao;
    }

    public void initializeLoggerSystem() {
        String logFileName;
        String logLevel;
        loadPropertiesFromFile();
        logFileName = AppPropertiesHolder.getInstance().getLogFilename(LOG_FILE_NAME_KEY);
        logLevel = AppPropertiesHolder.getInstance().getLogLevel(LOG_LEVEL_KEY);
        System.setProperty(LOG_FILE_NAME_KEY,logFileName);
        System.setProperty(LOG_LEVEL_KEY,logLevel);
        LoggerContext ctx =
                (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
        LOGGER = LoggerFactory.getLogger(Process.class);
    }

    private void loadPropertiesFromFile() {
        try {
            AppPropertiesHolder.getInstance().loadPropertiesFromFile();
        } catch (IOException e) {
            System.out.println("Cannot load property file \"" + AppPropertiesHolder.PROPERTIES_FILE_NAME + "\"");
        }
    }

    public void createDbTableIfNotExist() {
        dbDao.createDb();
    }

    public void runMainRoute(String parameter, String fileName) throws XmlParserException {
        switch (parameter) {
            case "-G": getDataFromDbAndWriteToFile(fileName); break;
            case "-C": clearDataInDb(); break;
            case "-S": synchroDataFromFileWithDataFromDb(fileName); break;
        }
    }

    private void synchroDataFromFileWithDataFromDb(String fileName) throws XmlParserException {
        Synchronizer synchronizer = new Synchronizer(dbDao, fileName);
        synchronizer.synchronize();
    }

    private void clearDataInDb() {
        dbDao.clearData();
    }

    private void getDataFromDbAndWriteToFile(String filename) {
        DbDataGetter dbDataGetter = new DbDataGetter(dbDao);
        Set<RawEntry> entries = dbDataGetter.getDataFromDb();

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Get data from DB:");
        for (RawEntry entry: entries) {
            LOGGER.debug(
                    "RawEntry: " + entry.getDepCode() + " "
                    + entry.getDepJob() + " "
                    + entry.getDescription());
        }

        XmlFileCreator xmlFileCreator = new XmlFileCreator(entries, filename);
        try {
            xmlFileCreator.convertDataToXmlAndSave();
        } catch (XMLStreamException e) {
            LOGGER.error("Create XML data error!", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found!", e);
        } catch (TransformerException e) {
            LOGGER.error("XML Transformation error!", e);
        }

        LOGGER.info("Get data success. Create file with data -\"" + filename + "\" success.");
    }
}
