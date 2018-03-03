package com.sapient.csvtoxml.xgen.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
public class CXSharedData{
	
	private final Map<String, String> map = new HashMap<>();
	
	private CXSharedData(){
		
	}
	
	private static class LazyHolder {
        private static final CXSharedData INSTANCE = new CXSharedData();
    }

    public static CXSharedData getInstance() {
        return LazyHolder.INSTANCE;
    }
    public void setAttribute(final String key,final String value){
    	map.put(key, value);
    }

    public String getAttribute(final String key){
    	return map.get(key);
    }
    
    public Map<String, String> get(){
    	return map;
    }
}
