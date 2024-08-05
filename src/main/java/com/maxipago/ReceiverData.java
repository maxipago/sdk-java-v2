package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class ReceiverData {
	@XmlElement
    public String firstName;
    @XmlElement
    public String lastName;
    @XmlElement
    public String taxIdNumber;
    @XmlElement
    public String walletAccountIdentification;
	
    public ReceiverData setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public ReceiverData setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    public ReceiverData setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
        return this;
    }
    public ReceiverData setWalletAccountIdentification(String walletAccountIdentification) {
        this.walletAccountIdentification = walletAccountIdentification;
        return this;
    }
}
