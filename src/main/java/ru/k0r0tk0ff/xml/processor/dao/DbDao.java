package ru.k0r0tk0ff.xml.processor.dao;

import ru.k0r0tk0ff.xml.processor.domain.RawEntry;

import javax.sql.rowset.CachedRowSet;
import java.util.Set;

/**
 * Created by korotkov_a_a on 21.10.2018.
 */
public interface DbDao {
    void createDb() throws DaoException;
    void clearData() throws DaoException;
    CachedRowSet getRawDataFromDb() throws DaoException;
    void deleteMissingEntriesInDb(Set<RawEntry> entriesForDeleteInDb) throws DaoException;
    void insertEntriesToDb(Set<RawEntry> entriesForInserToDb) throws DaoException;
    void updateEntriesInDb(Set<RawEntry> entriesForUpdateInDb) throws DaoException;
}
