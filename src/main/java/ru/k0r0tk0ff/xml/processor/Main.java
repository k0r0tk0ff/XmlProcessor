package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import ru.k0r0tk0ff.xml.processor.dao.H2DbDao;
import ru.k0r0tk0ff.xml.processor.infrastructure.AppPropertiesHolder;

import java.io.IOException;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {

    static {
        try {
            initializeLoggerSystem();
        } catch (IOException e) {
            System.out.println("Read the property file error! Check that the file exists and contains valid data.");
        }
    }
    public static void main(String[] args) {
        Process process = new Process(new H2DbDao(), args);
        process.execute();
    }

    private static void initializeLoggerSystem() throws IOException {
        AppPropertiesHolder appProperties = AppPropertiesHolder.getInstance();
        appProperties.loadPropertiesFromFile();
        appProperties.setLogFileName();
        appProperties.setLogLevel();
        LoggerContext ctx =
                (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}
