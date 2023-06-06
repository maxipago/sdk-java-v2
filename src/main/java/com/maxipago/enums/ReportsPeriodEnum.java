package com.maxipago.enums;

public enum ReportsPeriodEnum {
	
	TODAY("today"), 
	RANGE("range");
	
	public String value;
	
	ReportsPeriodEnum(String value){
		this.value = value;
	}
}
