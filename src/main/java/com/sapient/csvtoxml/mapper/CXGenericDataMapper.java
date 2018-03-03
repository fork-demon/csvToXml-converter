package com.sapient.csvtoxml.mapper;

import com.sapient.csvtoxml.common.util.CXCommonDTO;

public interface CXGenericDataMapper {
	
	public void mapdata(CXCommonDTO request) throws CXDataMapperException;

}
