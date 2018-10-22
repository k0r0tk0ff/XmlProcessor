package ru.k0r0tk0ff.xml.processor.service.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DaoException;
import ru.k0r0tk0ff.xml.processor.dao.DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by korotkov_a_a on 18.10.2018.
 */
public class DataConverter {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataConverter.class);

    private DbDao dbDao;

    public DataConverter(DbDao dbDao) {
        this.dbDao = dbDao;
    }

    public Set<RawEntry> convertDbDataToRawEntries() throws DaoException, DataConverterException {
        CachedRowSet cachedRowSet = dbDao.getRawDataFromDb();
        Set<RawEntry> entries = new HashSet<>();
        try {
            while (cachedRowSet.next()) {
                RawEntry rawEntry = new RawEntry();
                rawEntry.setDepCode(cachedRowSet.getString("DEPCODE"));
                rawEntry.setDepJob(cachedRowSet.getString("DEPJOB"));
                rawEntry.setDescription(cachedRowSet.getString("DESCRIPTION"));
                entries.add(rawEntry);
            }
            cachedRowSet.close();
        } catch (SQLException e) {
            throw new DataConverterException("Cannot create raw data from DB!");
        }
        return entries;
    }
}
