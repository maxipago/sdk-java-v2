package com.maxipago.enums;

public enum SdkUiTypeEnum {
	
	SINGLE_SELECT("SINGLE_SELECT"), 
	MULTI_SELECT("MULTI_SELECT"),
	OUT_OF_BAND("OUT_OF_BAND"),
	HTML_OTHER("HTML_OTHER");
	
	public String value;
	
	SdkUiTypeEnum(String value){
		this.value = value;
	}
}
