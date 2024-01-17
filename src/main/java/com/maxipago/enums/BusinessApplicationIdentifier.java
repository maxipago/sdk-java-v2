package com.maxipago.enums;

public enum BusinessApplicationIdentifier {
	CBPS("01");
	
	private String identifier;
	
	BusinessApplicationIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	@Override
	public String toString() {
		return this.identifier;
	}
}
