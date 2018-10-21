package ru.k0r0tk0ff.xml.processor.dao;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */

import com.sun.rowset.CachedRowSetImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.Set;

public class H2DbDao implements DbDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2DbDao.class);
    private final Connection connection = ConnectionHolder.getInstance().getConnection();

    public void createDb() throws DaoException {
        try (Statement statement = connection.createStatement()) {
            String createDbTable = "CREATE TABLE IF NOT EXISTS DATA " +
                    "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "DEPCODE VARCHAR(20), " +
                    "DEPJOB VARCHAR(100), " +
                    "DESCRIPTION VARCHAR(255))";
            String createIndex = "CREATE UNIQUE INDEX IF NOT EXISTS DEP_INDEX ON DATA(DEPCODE, DEPJOB)";
            statement.execute(createDbTable);
            statement.execute(createIndex);
            LOGGER.info("Check exist table in DB - success");
        } catch (SQLException e) {
            throw new DaoException("Cannot create or truncate db!",e);
        }
    }

    public void clearData() throws DaoException {
        try (Statement statement = connection.createStatement()) {
            String clearTableInDb = "TRUNCATE TABLE DATA";
            statement.execute(clearTableInDb);
            LOGGER.info("Clear data success");
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot clear data in db! (or close connection)",e);
        }
    }

    public CachedRowSet getRawDataFromDb() throws DaoException {
        CachedRowSet cachedRowSet = null;
        ResultSet resultSet = null;
        try(Statement statement = connection.createStatement()) {
            String sqlGetData = "SELECT DEPCODE, DEPJOB, DESCRIPTION FROM DATA";
            resultSet = statement.executeQuery(sqlGetData);
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
            if(resultSet != null){
                resultSet.close();
            }
            LOGGER.info("Get data from db success");
        } catch (SQLException e) {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e1) {
                    throw new DaoException("Close resultSet error!", e);
                }
            }
            throw new DaoException("Get data from db error!", e);
        }
        return cachedRowSet;
    }

    public void deleteMissingEntriesInDb(Set<RawEntry> entriesForDeleteInDb) throws DaoException {
        final String queryForDeleteData = "DELETE FROM DATA WHERE DEPCODE = ? AND DEPJOB = ?";
        int i = 0;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Connection.setAutoCommit(false) error!",e);
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryForDeleteData)) {
            for (RawEntry entry : entriesForDeleteInDb) {
                preparedStatement.setString(1, entry.getDepCode());
                preparedStatement.setString(2, entry.getDepJob());
                preparedStatement.addBatch();
                i++;
                if (i % 5000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info(" Delete data success. Total delete rows: " + i);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException("Cannot rollback data in DB !", e);
            }
            throw new DaoException("Cannot delete data in DB !", e);
        }
    }

    public void insertEntriesToDb(Set<RawEntry> entriesForInserToDb) throws DaoException {
        final String queryForInsertData = "INSERT INTO DATA (DEPCODE, DEPJOB, DESCRIPTION) values (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Connection.setAutoCommit(false) error!",e);
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryForInsertData)) {
            int i = 0;
            for (RawEntry entry : entriesForInserToDb) {
                preparedStatement.setString(1, entry.getDepCode());
                preparedStatement.setString(2, entry.getDepJob());
                preparedStatement.setString(3, entry.getDescription());
                preparedStatement.addBatch();
                i++;
                if (i % 5000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info(" Insert data success. Total insert rows: " + i);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException("Cannot rollback !",e1);
            }
            throw new DaoException("Cannot insert data to DB !",e);
        }
    }

    public void updateEntriesInDb(Set<RawEntry> entriesForUpdateInDb) throws DaoException {
        final String queryForInsertData = "UPDATE DATA SET DESCRIPTION = ? WHERE DEPCODE = ? AND DEPJOB = ?";
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Connection.setAutoCommit(false) error!",e);
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryForInsertData)) {
            int i = 0;
            for (RawEntry entry : entriesForUpdateInDb) {
                preparedStatement.setString(1, entry.getDescription());
                preparedStatement.setString(2, entry.getDepCode());
                preparedStatement.setString(3, entry.getDepJob());
                preparedStatement.addBatch();
                i++;
                if (i % 5000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info(" Update data success. Total update rows: " + i);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException("Cannot rollback !",e1);
            }
            throw new DaoException("Cannot update data in DB !",e);
        }
    }
}
