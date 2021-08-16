package com.maxipago.enums;

public enum SdkInterfaceEnum {
	
	NATIVE("NATIVE"),
	HTML("HTML"),
	BOTH("BOTH");
	
	public String value;
	
	SdkInterfaceEnum(String value){
		this.value = value;
	}
}
