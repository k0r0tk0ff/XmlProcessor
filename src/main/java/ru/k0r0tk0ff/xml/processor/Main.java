package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.infrastructure.io.utils.AppPropertiesHolder;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;

import java.io.IOException;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {
    private final static String LOG_FILE_NAME_KEY = "xml.processor.log.filename";

    public static void main(String[] args) {
        initializeLoggerSystem();
        Logger LOGGER = LoggerFactory.getLogger(Main.class);

        DbDao dbDao = new DbDao();
        dbDao.createDb();
        ConnectionHolder.getInstance().closeConnection();

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("System property xmlProcessorLogFileName = " + System.getProperty("xml.processor.log.filename"));
        }
    }

    private static void initializeLoggerSystem() {
        String logFileName;
        loadPropertiesFromFile();
        logFileName = AppPropertiesHolder.getInstance().getLogFilename(LOG_FILE_NAME_KEY);
        System.setProperty(LOG_FILE_NAME_KEY, logFileName);
        LoggerContext ctx =
                (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }

    private static void loadPropertiesFromFile() {
        try {
            AppPropertiesHolder.getInstance().loadPropertiesFromFile();
        } catch (IOException e) {
            System.out.println("Cannot load property file \"" + AppPropertiesHolder.PROPERTIES_FILE_NAME + "\"");
        }
    }
}
