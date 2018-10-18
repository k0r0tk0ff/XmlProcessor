package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.infrastructure.io.utils.AppPropertiesHolder;
import ru.k0r0tk0ff.xml.processor.service.XmlParser;

import java.io.IOException;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {
    public static void main(String[] args) {
        String logFilename;

        try {
            AppPropertiesHolder.getInstance().loadPropertiesFromFile();
        } catch (IOException e) {
            System.out.println("Cannot load property file \"" + AppPropertiesHolder.getPropertiesFileName() + "\"");
        }

        logFilename = AppPropertiesHolder.getInstance().getLogFilename();

        initializeLoggerSystem(logFilename);
        Logger LOGGER = LoggerFactory.getLogger(Main.class);

        LOGGER.debug("debug");
        LOGGER.info("info");
        LOGGER.warn("warning");

        XmlParser xmlParser = new XmlParser();
        xmlParser.testLog();

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("System property xmlProcessorLogFileName = " + System.getProperty("xmlProcessorLogFileName"));
        }
    }

    private static void initializeLoggerSystem(String fileName) {
        System.setProperty("xmlProcessorLogFileName", fileName);
        LoggerContext ctx =
                (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}
