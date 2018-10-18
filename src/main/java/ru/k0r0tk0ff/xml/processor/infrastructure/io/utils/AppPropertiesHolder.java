package ru.k0r0tk0ff.xml.processor.infrastructure.io.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class AppPropertiesHolder {
    private final static String PROPERTIES_FILE_NAME = "app.properties";
    private static volatile AppPropertiesHolder instance;
    private static Properties properties;

    public static AppPropertiesHolder getInstance() {
        AppPropertiesHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (AppPropertiesHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AppPropertiesHolder();
                }
            }
        }
        return localInstance;
    }

    public void loadPropertiesFromFile() throws IOException {
        properties = new Properties();
        Path path = Paths.get(PROPERTIES_FILE_NAME);
        BufferedReader input = Files.newBufferedReader(path, Charset.forName("UTF-8"));
        properties.load(input);
        input.close();
    }

    public String getLogFilename() {
        return properties.getProperty("xmlProcessorLogFileName");
    }

    public static Properties getProperties(){
        return properties;
    }

    public static String getPropertiesFileName() {
        return PROPERTIES_FILE_NAME;
    }
}
