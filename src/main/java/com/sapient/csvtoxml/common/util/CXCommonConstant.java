package com.sapient.csvtoxml.common.util;

public class CXCommonConstant {

	/*
	 * static final String TRADE="TRADE"; static final String
	 * SETTLEMENT="SETTLEMENT"; static final String ORDER="ORDER"; static final
	 * String CONFIRMATION="CONFIRMATION";
	 */

	public static final String FILE_EXTN = "csv";

	public enum CSVTYPES {
		TRADE("TRADE"), SETTLEMENT("SETTLEMENT"), ORDER("ORDER"), CONFIRMATION("CONFIRMATION");

		private final String type; 

		CSVTYPES(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

	}
	
	


}
