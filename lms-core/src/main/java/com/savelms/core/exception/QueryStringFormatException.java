package com.savelms.core.exception;

import javax.swing.text.html.HTMLDocument.RunElement;

public class QueryStringFormatException extends
    RuntimeException {

    public QueryStringFormatException() {
        super();
    }

    public QueryStringFormatException(String message) {
        super(message);
    }

    public QueryStringFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryStringFormatException(Throwable cause) {
        super(cause);
    }
}
