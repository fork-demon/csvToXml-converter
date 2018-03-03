package com.sapient.csvtoxml.error;

import com.sapient.csvtoxml.common.util.CXErrorCodes;

public class XmlCreationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3083372512897750331L;
	
	private String error;
	
	public XmlCreationException(String message){
		
		super();
		this.error = message;
		
		
	}
	
	public XmlCreationException(String code ,String message){
		
		super(message);
		this.error = code;
	}
	
	
	public XmlCreationException(String code  , String message, Throwable throwable){
		
		super(message, throwable);
		this.error = code;
		
	}
	
	

}
