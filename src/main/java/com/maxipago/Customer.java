package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class Customer {
    @XmlElement
    public String name;

    @XmlElement
    public String address;

    @XmlElement
    public String address1;

    @XmlElement
    public String address2;

    @XmlElement
    public String district;

    @XmlElement
    public String city;

    @XmlElement
    public String state;

    public Customer setAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    @XmlElement(name = "postalcode")
    public String postalCode;

    @XmlElement
    public String country;

    @XmlElement
    public String phone;

    @XmlElement
    public String email;

    @XmlElement
    public String companyName;

    @XmlElement
    public String addressType;

    @XmlElement
    public String addressNumber;

    @XmlElement
    public String deliveryDate;

    @XmlElement
    public String shippingType;

    @XmlElement
    public String id = "123";

    @XmlElement
    public String type;

    @XmlElement
    public String gender;

    @XmlElement
    public String birthDate;

    @XmlElement
    public String zip;

    public Customer setZip(String zip) {
        this.zip = zip;
        return this;
    }

    @XmlElement(name = "phones")
    public Phones phones;

    @XmlElement(name = "documents")
    public Documents documents;

    public Customer addPhone(Phone phone) {
        if (phones == null) {
            phones = new Phones();
        }

        phones.phone.add(phone);

        return this;
    }

    public Customer addDocument(Document document) {
        if (documents == null) {
            documents = new Documents();
        }

        documents.document.add(document);

        return this;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public Customer setAddress(String address) {
        this.address = address;
        return this;
    }

    public Customer setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public Customer setDistrict(String district) {
        this.district = district;
        return this;
    }

    public Customer setCity(String city) {
        this.city = city;
        return this;
    }

    public Customer setState(String state) {
        this.state = state;
        return this;
    }

    public Customer setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Customer setCountry(String country) {
        this.country = country;
        return this;
    }

    public Customer setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }

    public Customer setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public Customer setAddressType(String addressType) {
        this.addressType = addressType;
        return this;
    }

    public Customer setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
        return this;
    }

    public Customer setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public Customer setShippingType(String shippingType) {
        this.shippingType = shippingType;
        return this;
    }

    public Customer setId(String id) {
        this.id = id;
        return this;
    }

    public Customer setType(String type) {
        this.type = type;
        return this;
    }

    public Customer setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Customer setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }
}
