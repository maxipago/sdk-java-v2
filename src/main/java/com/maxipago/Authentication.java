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
    @XmlElement
    public String responseMode;
    @XmlElement
    public String sendNotification;

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
    
    public Authentication(String mpiProcessorID, String onFailure, ChallengePreference challengePreference,String responseMode, String sendNotification) {
        this.mpiProcessorID = mpiProcessorID;
        this.onFailure = onFailure;
        this.challengePreference = challengePreference;
        this.responseMode = responseMode;
        this.sendNotification = sendNotification;
    }

    public Authentication setOnFailure(String onFailure) {
        this.onFailure = onFailure;
        return this;
    }
}
