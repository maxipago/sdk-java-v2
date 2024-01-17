package com.maxipago.enums;

public enum SDWOProcessingType {
	PURCHASE("Purchase"), CASH_IN("Cash-in");
	
	private String processingType;
	
	SDWOProcessingType(String processingType){
		this.processingType = processingType;
	}
	
	@Override
	public String toString() {
		return this.processingType;
	}
}
