package ru.k0r0tk0ff.xml.processor.infrastructure;

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
    public final static String PROPERTIES_FILE_NAME = "app.properties";

    private static AppPropertiesHolder instance;
    private static Properties properties;

    private AppPropertiesHolder(){
    }

    public static AppPropertiesHolder getInstance() {
        if (instance == null) {
            instance = new AppPropertiesHolder();
        }
        return instance;
    }

    public void loadPropertiesFromFile() throws IOException {
        properties = new Properties();
        Path path = Paths.get(PROPERTIES_FILE_NAME);
        BufferedReader input = Files.newBufferedReader(path, Charset.forName("UTF-8"));
        properties.load(input);
        input.close();
    }

    public String getLogFilename(String logFileNamePropertyKey) {
        return properties.getProperty(logFileNamePropertyKey);
    }

    public Properties getProperties(){
        return properties;
    }

    public String getLogLevel(String logLevelKey) {
        return properties.getProperty(logLevelKey);
    }
}
