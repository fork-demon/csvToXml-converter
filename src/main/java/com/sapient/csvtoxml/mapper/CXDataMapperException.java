package com.sapient.csvtoxml.mapper;

import com.sapient.csvtoxml.common.util.CXErrorCodes;

public class CXDataMapperException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3083372512897750331L;
	
	private CXErrorCodes error;
	
	public CXDataMapperException(CXErrorCodes code){
		
		super();
		this.error = code;
		
		
	}
	
	public CXDataMapperException(CXErrorCodes code ,String message){
		
		super(message);
		this.error = code;
	}
	
	
	public CXDataMapperException(CXErrorCodes code  , String message, Throwable throwable){
		
		super(message, throwable);
		this.error = code;
		
	}
	
	

}
