package com.sapient.csvtoxml.error;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CXExceptionHandler {

	@ExceptionHandler(CXInvalidDSException.class)
	public ResponseEntity<?> handleInvalidContentException(CXInvalidDSException rnfe, HttpServletRequest request) {

		CXErrorDetail errorDetail = new CXErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle("Invalid Content");
		errorDetail.setDetail(rnfe.getMessage());
		errorDetail.setDeveloperMessage(rnfe.getClass().getName());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CXInvalidFormatException.class)
	public ResponseEntity<?> handleFileFormatException(CXInvalidFormatException rnfe, HttpServletRequest request) {

		CXErrorDetail errorDetail = new CXErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle("Invalid File Format");
		errorDetail.setDetail(rnfe.getMessage());
		errorDetail.setDeveloperMessage(rnfe.getClass().getName());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CXServerException.class)
	public ResponseEntity<?> handleServerException(CXServerException rnfe, HttpServletRequest request) {

		CXErrorDetail errorDetail = new CXErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setTitle("Server Processing failed");
		errorDetail.setDetail(rnfe.getMessage());
		errorDetail.setDeveloperMessage(rnfe.getClass().getName());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}