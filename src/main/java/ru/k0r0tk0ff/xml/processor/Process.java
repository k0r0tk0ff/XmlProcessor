package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.infrastructure.io.utils.AppPropertiesHolder;
import ru.k0r0tk0ff.xml.processor.service.DataCreatorForXmlFile;
import ru.k0r0tk0ff.xml.processor.service.XmlFileCreator;

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
    private static Logger LOGGER;

    private DbDao dbDao;

    public void setDbDao(DbDao dbDao) {
        this.dbDao = dbDao;
    }

    public void initializeLoggerSystem() {
        String logFileName;
        loadPropertiesFromFile();
        logFileName = AppPropertiesHolder.getInstance().getLogFilename(LOG_FILE_NAME_KEY);
        System.setProperty(LOG_FILE_NAME_KEY,logFileName);
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

    public void runMainRoute(String parameter, String fileName) {
        switch (parameter) {
            case "-G": getDataFromDbAndWriteToFile(fileName); break;
        }
    }

    private void getDataFromDbAndWriteToFile(String filename) {
        DataCreatorForXmlFile dataCreatorForXmlFile = new DataCreatorForXmlFile(dbDao);
        Set<RawEntry> entries = dataCreatorForXmlFile.getDataFromDb();

        if(LOGGER.isDebugEnabled())
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
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        LOGGER.info("Get data success. Create file with data -\"" + filename + "\" success.");
    }
}
