package ru.k0r0tk0ff.xml.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.xml.processor.dao.DaoException;
import ru.k0r0tk0ff.xml.processor.dao.H2DbDao;
import ru.k0r0tk0ff.xml.processor.domain.RawEntry;
import ru.k0r0tk0ff.xml.processor.service.converter.DataConverterException;
import ru.k0r0tk0ff.xml.processor.utils.input.InputParametersChecker;
import ru.k0r0tk0ff.xml.processor.utils.input.ParametersCheckException;
import ru.k0r0tk0ff.xml.processor.infrastructure.store.db.ConnectionHolder;
import ru.k0r0tk0ff.xml.processor.service.converter.DataConverter;
import ru.k0r0tk0ff.xml.processor.service.Synchronizer;
import ru.k0r0tk0ff.xml.processor.service.XmlFileCreator;
import ru.k0r0tk0ff.xml.processor.service.parser.XmlParserException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */
public class Process {
    private static final Logger LOGGER = LoggerFactory.getLogger(Process.class);

    private H2DbDao h2DbDao;
    private String[] args;
    private Connection connection;

    public Process(H2DbDao h2DbDao, String[] args) {
        this.h2DbDao = h2DbDao;
        this.args = args;
        this.connection = ConnectionHolder.getInstance().getConnection();
    }

    public void execute() {
        String fileName = "";
        try {
            InputParametersChecker.parametersCheck(args);
            prepareWorkSpace();

            if(args.length == 2) {
                fileName = args[1];
            }

            try {
                switch (args[0]) {
                    case "-u":
                        getDataFromDbAndWriteToFile(fileName);
                        connection.close();
                        break;
                    case "-c":
                        clearDataInDb();
                        connection.close();
                        break;
                    case "-s":
                        synchronize(fileName);
                        connection.close();
                        break;
                }
            } catch (SQLException e) {
                throw new DaoException("Cannot close connection!",e);
            } catch (DataConverterException e) {
                e.printStackTrace();
            }
            System.out.println("Work complete.");
        }  catch (XmlParserException e) {
            LOGGER.error("Parse XML file error!", e);
        } catch (XMLStreamException e) {
            LOGGER.error("Create XML data error!", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("Incorrect file name for upload file.");
        } catch (TransformerException e) {
            LOGGER.error("XML Transformation error!", e);
        } catch (ParametersCheckException e) {
            LOGGER.error("Input parameters error!", e);
        } catch (DaoException e) {
            LOGGER.error("Data access object exception!", e);
        }
    }

    private void prepareWorkSpace() throws DaoException {
        h2DbDao.createDb();
    }

    private void synchronize(String fileName)
            throws XmlParserException, DaoException, DataConverterException {
        Synchronizer synchronizer = new Synchronizer(h2DbDao, fileName);
        synchronizer.synchronize();
    }

    private void clearDataInDb() throws DaoException {
        h2DbDao.clearData();
    }

    private void getDataFromDbAndWriteToFile(String filename) throws
            ParametersCheckException,
            FileNotFoundException,
            XMLStreamException,
            TransformerException,
            DaoException,
            DataConverterException {
        DataConverter dataConverter = new DataConverter(h2DbDao);
        Set<RawEntry> entries = dataConverter.convertDbDataToRawEntries();

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Get data from DB:");
        for (RawEntry entry: entries) {
            LOGGER.debug(
                    "RawEntry: " + entry.getDepCode() + " "
                    + entry.getDepJob() + " "
                    + entry.getDescription());
        }

        XmlFileCreator xmlFileCreator = new XmlFileCreator(entries, filename);
        xmlFileCreator.convertDataToXmlAndSave();
        LOGGER.info("Get data success. Create file with data -\"" + filename + "\" success.");
    }
}
