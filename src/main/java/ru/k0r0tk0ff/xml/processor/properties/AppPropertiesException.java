package ru.k0r0tk0ff.xml.processor.properties;

/**
 * Created by korotkov_a_a on 22.10.2018.
 */
public class AppPropertiesException extends Exception {
    public AppPropertiesException() {
        super();
    }

    public AppPropertiesException(String message) {
        super(message);
    }

    public AppPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppPropertiesException(Throwable cause) {
        super(cause);
    }

    protected AppPropertiesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
