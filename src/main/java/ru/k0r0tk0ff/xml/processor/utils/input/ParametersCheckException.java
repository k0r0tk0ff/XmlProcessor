package ru.k0r0tk0ff.xml.processor.utils.input;

/**
 * Created by korotkov_a_a on 21.10.2018.
 */
public class ParametersCheckException extends Exception {
    public ParametersCheckException() {
        super();
    }

    public ParametersCheckException(String message) {
        super(message);
    }

    public ParametersCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParametersCheckException(Throwable cause) {
        super(cause);
    }

    protected ParametersCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
