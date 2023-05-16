package com.maxipago.enums;

public enum ChallengePreference {
	
	NO_PREFERENCE("NO_PREFERENCE"),
	CHALLENGE_REQUESTED("CHALLENGE_REQUESTED"),
	NO_CHALLENGE_REQUESTED("NO_CHALLENGE_REQUESTED"),
	CHALLENGE_MANDATED("CHALLENGE_MANDATED"),
	DATA_ONLY("DATA_ONLY");
	
	public String value;
	
	ChallengePreference(String value){
		this.value = value;
	}
}
