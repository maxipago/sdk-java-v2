package com.maxipago;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FraudDetails {
    @XmlElement
    public String fraudProcessorID;

    @XmlElement
    public String captureOnLowRisk = "N";

    @XmlElement
    public String voidOnHighRisk = "N";

    @XmlElement
    public String websiteId;

    @XmlElement
    public String fraudToken;

    public FraudDetails setFraudProcessorID(String fraudProcessorID) {
        this.fraudProcessorID = fraudProcessorID;
        return this;
    }

    public FraudDetails setCaptureOnLowRisk(String captureOnLowRisk) {
        this.captureOnLowRisk = captureOnLowRisk;
        return this;
    }

    public FraudDetails setVoidOnHighRisk(String voidOnHighRisk) {
        this.voidOnHighRisk = voidOnHighRisk;
        return this;
    }

    public FraudDetails setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
        return this;
    }

    public FraudDetails setFraudToken(String fraudToken) {
        this.fraudToken = fraudToken;
        return this;
    }
}
