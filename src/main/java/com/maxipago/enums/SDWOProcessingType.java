package com.maxipago.enums;

public enum SDWOProcessingType {
	PURCHASE("01"), CASH_IN("02");
	
	private String processingType;
	
	SDWOProcessingType(String processingType){
		this.processingType = processingType;
	}
	
	@Override
	public String toString() {
		return this.processingType;
	}
}
