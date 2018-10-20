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

public class DbDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbDao.class);

    public void createDb() {
        try (Statement statement = ConnectionHolder.getInstance().getConnection().createStatement()) {
            String createDbTable = "CREATE TABLE IF NOT EXISTS DATA " +
                    "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "DEPCODE VARCHAR(20), " +
                    "DEPJOB VARCHAR(100), " +
                    "DESCRIPTION VARCHAR(255))";
            String createIndex = "CREATE UNIQUE INDEX IF NOT EXISTS DEP_INDEX ON DATA(DEPCODE, DEPJOB)";
            statement.execute(createDbTable);
            statement.execute(createIndex);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table in DB is exist");
            }
        } catch (SQLException e) {
            LOGGER.error("Cannot create or truncate db!", e);
        }
    }

    public void clearData() {
        try (Statement statement = ConnectionHolder.getInstance().getConnection().createStatement()) {
            String clearTableInDb = "TRUNCATE TABLE DATA";
            statement.execute(clearTableInDb);
            LOGGER.info("Clear data success");
            statement.close();
        } catch (SQLException e) {
            LOGGER.error("Cannot clear data in db!", e);
        }
    }

    public CachedRowSet getRawDataFromDb(){
        CachedRowSet cachedRowSet = null;
        try(Statement statement = ConnectionHolder.getInstance()
                .getConnection()
                .createStatement()) {
            String sqlGetData = "SELECT DEPCODE, DEPJOB, DESCRIPTION FROM DATA";
            ResultSet resultSet = statement.executeQuery(sqlGetData);
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
            if(resultSet != null){
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Get data from db error!", e);
        }
        return cachedRowSet;
    }

    public void deleteMissingEntriesInDb(Set<RawEntry> entriesForDeleteInDb) {
        final String queryForDeleteData = "DELETE FROM DATA WHERE DEPCODE = ? AND DEPJOB = ?";
        int i = 0;
        try (PreparedStatement preparedStatement =
                     ConnectionHolder.getInstance().getConnection().prepareStatement(queryForDeleteData)) {
            ConnectionHolder.getInstance().getConnection().setAutoCommit(false);
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
            ConnectionHolder.getInstance().getConnection().commit();
            ConnectionHolder.getInstance().getConnection().setAutoCommit(true);
            LOGGER.debug(" Delete data success. Total delete rows: " + i);
        } catch (SQLException e) {
            try {
                ConnectionHolder.getInstance().getConnection().rollback();
            } catch (SQLException e1) {
                LOGGER.error("Cannot rollback !", e1);
            }
            LOGGER.error("Cannot delete data in DB !", e);
        }
    }

    public void insertEntriesToDb(Set<RawEntry> entriesForInserToDb) {
        final String queryForInsertData = "INSERT INTO DATA (DEPCODE, DEPJOB, DESCRIPTION) values (?, ?, ?)";

        try (PreparedStatement preparedStatement =
                     ConnectionHolder.getInstance().getConnection().prepareStatement(queryForInsertData)) {
            ConnectionHolder.getInstance().getConnection().setAutoCommit(false);
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
            ConnectionHolder.getInstance().getConnection().commit();
            ConnectionHolder.getInstance().getConnection().setAutoCommit(true);
            LOGGER.debug(" Insert data success. Total insert rows: " + i);
        } catch (SQLException e) {
            try {
                ConnectionHolder.getInstance().getConnection().rollback();
            } catch (SQLException e1) {
                LOGGER.error("Cannot rollback !", e1);
            }
            LOGGER.error("Cannot insert data to DB !", e);
        }
    }

    public void updateEntriesInDb(Set<RawEntry> entriesForUpdateInDb) {
        final String queryForInsertData = "UPDATE DATA SET DESCRIPTION = ? WHERE DEPCODE = ? AND DEPJOB = ?";

        try (PreparedStatement preparedStatement =
                     ConnectionHolder.getInstance().getConnection().prepareStatement(queryForInsertData)) {
            ConnectionHolder.getInstance().getConnection().setAutoCommit(false);
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
            ConnectionHolder.getInstance().getConnection().commit();
            ConnectionHolder.getInstance().getConnection().setAutoCommit(true);
            LOGGER.debug(" Update data success. Total update rows: " + i);
        } catch (SQLException e) {
            try {
                ConnectionHolder.getInstance().getConnection().rollback();
            } catch (SQLException e1) {
                LOGGER.error("Cannot rollback !", e1);
            }
            LOGGER.error("Cannot update data in DB !", e);
        }
    }
}
