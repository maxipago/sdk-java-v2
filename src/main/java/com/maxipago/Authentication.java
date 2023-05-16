package com.maxipago;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.maxipago.enums.ChallengePreference;

@XmlRootElement
public class Authentication {
    public static String DECLINE = "decline";
    public static String CONTINUE = "continue";
    public ChallengePreference challengePreference = ChallengePreference.NO_PREFERENCE;    
    
    @XmlElement
    public String mpiProcessorID;

    @XmlElement
    public String onFailure;

    public Authentication() {
    }

    public Authentication(String mpiProcessorID, String onFailure) {
        this.mpiProcessorID = mpiProcessorID;
        this.onFailure = onFailure;
    }

    public Authentication setMpiProcessorID(String mpiProcessorID) {
        this.mpiProcessorID = mpiProcessorID;
        return this;
    }

    public Authentication(String mpiProcessorID, String onFailure, ChallengePreference challengePreference) {
        this.mpiProcessorID = mpiProcessorID;
        this.onFailure = onFailure;
        this.challengePreference = challengePreference;
    }

    public Authentication setOnFailure(String onFailure) {
        this.onFailure = onFailure;
        return this;
    }
}
