package com.sapient.csvtoxml.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sapient.csvtoxml.common.util.CXCommonDTO;
import com.sapient.csvtoxml.mapper.CXDataMapperException;
import com.sapient.csvtoxml.mapper.CXGenericDataMapper;
import com.sapient.csvtoxml.xgen.factory.CXDynamicFactory;

@Component
public class CXUploadService {
	
	@Autowired
	CXDynamicFactory dynamicFactory;
	
	@Autowired
	CXGenericDataMapper dataMapper;
	
	public void execute(String csvType,Map<String,String> dataMap) throws CXDataMapperException{
		System.out.println("ENTRY csvType "+csvType);
		System.out.println(dynamicFactory);
		String packageloc=dynamicFactory.getPaclageLoc(csvType);
		System.out.println("packagaloc "+packageloc);
		CXCommonDTO commonDto=new CXCommonDTO();
		commonDto.setClassPath(packageloc);
		commonDto.setDataMap(dataMap);
		System.out.println("Before calling mapData");
		dataMapper.mapdata(commonDto);
		System.out.println("after calling mapData");
		
	}
	

}
