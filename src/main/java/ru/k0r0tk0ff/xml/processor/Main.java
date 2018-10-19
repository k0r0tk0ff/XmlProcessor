package ru.k0r0tk0ff.xml.processor;

import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.infrastructure.io.utils.InputParametersChecker;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class Main {

    public static void main(String[] args) {
        InputParametersChecker.ParametersCheck(args[0], args[1]);
        Process process = new Process();
        process.initializeLoggerSystem();
        process.setDbDao(new DbDao());
        process.createDbTableIfNotExist();
        process.runMainRoute(args[0], args[1]);



        ConnectionHolder.getInstance().closeConnection();
    }
}
