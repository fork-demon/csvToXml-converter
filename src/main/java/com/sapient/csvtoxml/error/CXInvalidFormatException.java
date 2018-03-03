package com.sapient.csvtoxml.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class CXInvalidFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CXInvalidFormatException() {}

    public CXInvalidFormatException(String message) {
            super(message);
    }

    public CXInvalidFormatException(String message, Throwable cause) {
            super(message, cause);
    }
}