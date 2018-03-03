package com.sapient.csvtoxml.common.util;

public enum CXErrorCodes {

	INVALID_REQUEST_DATA(400),
	CLASS_LOADING_FAILED(501),
	REFLECTION_FAILED(501),
	JAXB_EXCEPTION(501)
	
	;

	private int code;

	CXErrorCodes(int code){

		this.code = code;
	}

	public int getCode(){

		return code;

	}
}
