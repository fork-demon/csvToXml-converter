package com.sapient.csvtoxml.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CXCommonUtil {

	
	public String getFileExtn(String fileName){
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		} 
		return extension;
	}
	
	
	
	public Map<String,String> createReqData(String headerLine,String dataLine){
		HashMap<String,String> dataMap=new HashMap<>();
		String header[] = headerLine.split(",");
		String data[]=dataLine.split(",");
		for(int i=0;i<header.length;i++){
			dataMap.put(header[i].toUpperCase(),data[i]);
		}
		System.out.println(dataMap);
		return dataMap;
	}
	
	public String getCSVType(String fileName){
		String csvType=null;
		System.out.println("NETRY fileName"+fileName);
		if(StringUtils.containsIgnoreCase(fileName,CXCommonConstant.CSVTYPES.CONFIRMATION.getType())){
			csvType=CXCommonConstant.CSVTYPES.CONFIRMATION.getType();
		}else if(StringUtils.containsIgnoreCase(fileName,CXCommonConstant.CSVTYPES.TRADE.getType())){
			csvType=CXCommonConstant.CSVTYPES.TRADE.getType();
		}else if(StringUtils.containsIgnoreCase(fileName,CXCommonConstant.CSVTYPES.ORDER.getType())){
			csvType=CXCommonConstant.CSVTYPES.ORDER.getType();
		}else if(StringUtils.containsIgnoreCase(fileName,CXCommonConstant.CSVTYPES.SETTLEMENT.getType())){
			csvType=CXCommonConstant.CSVTYPES.SETTLEMENT.getType();
		}
		System.out.println("getCSVType "+csvType);
		return csvType;
	
	}
	
	
}
