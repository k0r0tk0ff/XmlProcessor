package ru.k0r0tk0ff.xml.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import ru.k0r0tk0ff.xml.processor.dao.H2DbDao;
import ru.k0r0tk0ff.xml.processor.properties.AppPropertiesException;
import ru.k0r0tk0ff.xml.processor.properties.AppPropertiesHolder;

import java.io.IOException;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {

    public static void main(String[] args) {
        try {
            initializeLoggerSystem();
            Process process = new Process(new H2DbDao(), args);
            process.execute();
        } catch (IOException e) {
            System.out.println("Read the property file error! Check that the file exists and contains valid data.");
        } catch (AppPropertiesException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initializeLoggerSystem() throws IOException, AppPropertiesException {
        AppPropertiesHolder appProperties = AppPropertiesHolder.getInstance();
        appProperties.loadPropertiesFromFile();
        appProperties.checkDataBaseProperties();
        appProperties.setLogFileName();
        appProperties.setLogLevel();
        LoggerContext ctx =
                (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}
