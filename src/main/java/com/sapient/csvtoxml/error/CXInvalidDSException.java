package com.sapient.csvtoxml.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CXInvalidDSException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CXInvalidDSException() {}

    public CXInvalidDSException(String message) {
            super(message);
    }

    public CXInvalidDSException(String message, Throwable cause) {
            super(message, cause);
    }
}