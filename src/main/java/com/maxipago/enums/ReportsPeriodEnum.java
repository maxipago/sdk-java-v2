package com.maxipago.enums;

public enum ReportsPeriodEnum {
	
	TODAY("today"), 
	RANGE("range"),
	LAST_SEVEN("last7"),
	LAST_THIRTY("last30"),
	THIS_MONTH("thismonth"),
	LAST_MONTH("lastmonth"),
	YESTERDAY("yesterday"),
	THIS_WEEK("thisweek");
	
	public String value;
	
	ReportsPeriodEnum(String value){
		this.value = value;
	}
}
