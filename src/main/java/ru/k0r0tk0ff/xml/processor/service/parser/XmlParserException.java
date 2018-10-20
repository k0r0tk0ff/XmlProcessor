package ru.k0r0tk0ff.xml.processor.service.parser;

/**
 * Created by korotkov_a_a on 20.10.2018.
 */
public class XmlParserException extends Exception {
    public XmlParserException() {
        super();
    }

    public XmlParserException(String message) {
        super(message);
    }

    public XmlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlParserException(Throwable cause) {
        super(cause);
    }

    protected XmlParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
