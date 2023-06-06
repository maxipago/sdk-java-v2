package com.maxipago.enums;

public enum ReportsPeriodEnum {
	
	TODAY("today"), 
	LAST_MONTH("lastmonth"), 
	RANGE("range");
	
	public String value;
	
	ReportsPeriodEnum(String value){
		this.value = value;
	}
}
