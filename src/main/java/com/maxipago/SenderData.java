package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class SenderData {

    @XmlElement
    public String taxIdNumber;
	
    public SenderData setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
        return this;
    }
}
