package ru.k0r0tk0ff.xml.processor.dao;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.WebRowSetImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.WebRowSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Truncate table success");
            }
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

/*    private void doInsertDataToDb(HashSet<RawEntry> hashSet) {
        DataCreatorForXmlFile dataCreator = new DataCreatorForXmlFile();

        final String queryForPutData = "INSERT into ARTICLE (NAME, CODE, GUID, USERNAME) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement =
                     dbStorage.getConnection().prepareStatement(queryForPutData)) {
            for (int i = 1; i < maxCountOfRawEntry + 1; i++) {
                preparedStatement.setString(1, dataForInsertToDb.get(i).getName());
                preparedStatement.setInt(2, dataForInsertToDb.get(i).getCode());
                preparedStatement.setString(3, dataForInsertToDb.get(i).getGuid());
                preparedStatement.setString(4, dataForInsertToDb.get(i).getUserName());
                preparedStatement.addBatch();
                if (i % 5000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            LOGGER.debug("Insert data success");
        } catch (SQLException e) {
            LOGGER.error("Cannot insert data to DB !", e);
        }
    }

    public HashSet<Entry> getDataFromDb() {
        HashSet<Entry> data = new ArrayList<>();
        String sqlQueryForGetData = "SELECT ID_ART, NAME, CODE, GUID, USERNAME FROM ARTICLE";
        try (Statement statement =
                     dbStorage.getConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlQueryForGetData)) {
                while (resultSet.next()) {
                    RawEntry rawEntry = new RawEntry();
                    rawEntry.setName(resultSet.getString("NAME"));
                    rawEntry.setId_art(resultSet.getInt("ID_ART"));
                    rawEntry.setCode(resultSet.getInt("CODE"));
                    rawEntry.setGuid(resultSet.getString("GUID"));
                    rawEntry.setUserName(resultSet.getString("USERNAME"));
                    data.add(rawEntry);
                }
            }
        } catch (SQLException e) {
            throw new StorageException("Cannot get data from DB !", e);
        }
        return data;
    }*/
}
