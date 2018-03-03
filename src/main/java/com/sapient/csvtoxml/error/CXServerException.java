package com.sapient.csvtoxml.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CXServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CXServerException() {}

    public CXServerException(String message) {
            super(message);
    }

    public CXServerException(String message, Throwable cause) {
            super(message, cause);
    }
}
