package com.sapient.csvtoxml.common.util;

import java.util.Map;

public class CXCommonDTO {
	private String classPath;
	private Map<String,String> dataMap;
	
	
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	

}
