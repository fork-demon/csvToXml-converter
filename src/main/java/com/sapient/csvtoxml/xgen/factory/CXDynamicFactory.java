package com.sapient.csvtoxml.xgen.factory;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.sapient.csvtoxml.xgen.model.CXSharedData;

@Component
public class CXDynamicFactory {
	
	
	
	public static String getPaclageLoc(String csvType){
		System.out.println("getpaclageLoc = "+csvType);
		if(null == csvType)
			throw new IllegalArgumentException("Please provide the csv file type.");
		
		CXSharedData context = CXSharedData.getInstance();
		System.out.println("CXDynamic----------- "+context);
		for(Entry<String, String> entry : context.get().entrySet() ){
			
			if(entry.getValue().toUpperCase().contains(csvType.toUpperCase())){
				return entry.getValue();
			}
			
		}
		
		return null;
	}

}
