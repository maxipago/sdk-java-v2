package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class SenderData {

    @XmlElement
    public String taxIdNumber;

    @XmlElement
    public String firstName;
	
    @XmlElement
    public String lastName;

    @XmlElement
    public String address;
	
    @XmlElement
    public String city;
	
    @XmlElement
    public String country;

    public SenderData setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
        return this;
    }

    public SenderData setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SenderData setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public SenderData setAddress(String address) {
        this.address = address;
        return this;
    }

    public SenderData setCity(String city) {
        this.city = city;
        return this;
    }

    public SenderData setCountry(String country) {
        this.country = country;
        return this;
    }
}
