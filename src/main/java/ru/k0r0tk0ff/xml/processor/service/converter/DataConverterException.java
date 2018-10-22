package ru.k0r0tk0ff.xml.processor.service.converter;

/**
 * Created by korotkov_a_a on 22.10.2018.
 */
public class DataConverterException extends Exception {
    public DataConverterException() {
        super();
    }

    public DataConverterException(String message) {
        super(message);
    }

    public DataConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataConverterException(Throwable cause) {
        super(cause);
    }

    protected DataConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
