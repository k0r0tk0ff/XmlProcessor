package ru.k0r0tk0ff.xml.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.infrastructure.io.utils.InputParametersChecker;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParserException;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {
    public static void main(String[] args) {
        InputParametersChecker.ParametersCheck(args);
        Process process = new Process();
        process.initializeLoggerSystem();
        Logger LOGGER = LoggerFactory.getLogger(Main.class);
        process.setDbDao(new DbDao());
        process.createDbTableIfNotExist();
        try {
            process.runMainRoute(args);
        } catch (XmlParserException e) {
            LOGGER.error("Parse XML file error!");
        }
        LOGGER.warn("Work complete.");
        ConnectionHolder.getInstance().closeConnection();
    }
}
