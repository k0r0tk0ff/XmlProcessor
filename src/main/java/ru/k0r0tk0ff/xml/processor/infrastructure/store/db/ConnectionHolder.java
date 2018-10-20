package ru.k0r0tk0ff.xml.processor.infrastructure.store.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.infrastructure.AppPropertiesHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */

public class ConnectionHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHolder.class);

    private static ConnectionHolder instance;
    private static Connection connection = null;

    private ConnectionHolder(){
    }

    public static ConnectionHolder getInstance() {
        if (instance == null) {
                    instance = new ConnectionHolder();
                    instance.initializeDataBaseResources();
                }
        return instance;
    }

    private void initializeDataBaseResources() {
        Properties properties = AppPropertiesHolder.getInstance().getProperties();
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password"));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Get connection success");
            }
        } catch (SQLException e) {
            LOGGER.error("ConnectionHolder Error!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Close connection success");
                }
            } catch (SQLException e) {
                LOGGER.error("Error close connection!", e);
            }

        }
    }
}
