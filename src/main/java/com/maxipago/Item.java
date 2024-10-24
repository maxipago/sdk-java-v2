package com.maxipago;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item {
    @XmlElement
    public Integer itemIndex;

    @XmlElement
    public String itemProductCode;

    @XmlElement
    public String itemDescription;

    @XmlElement
    public Integer itemQuantity;

    @XmlElement
    public Double itemTotalAmount;

    @XmlElement
    public Double itemUnitCost;

    @XmlElement
    public Integer sellerMcc;

    @XmlElement
    public String sellerId;

    @XmlElement
    public String sellerAddress;

    @XmlElement
    public String sellerCity;

    @XmlElement
    public String sellerState;

    @XmlElement
    public String sellerCountry;

    @XmlElement
    public String sellerCep;

    @XmlElement
    public String sellerTaxId;

    public Item() {}

    public Item(Integer itemIndex, String itemProductCode, String itemDescription, Integer itemQuantity, Double itemTotalAmount, Double itemUnitCost) {
        this.itemIndex = itemIndex;
        this.itemProductCode = itemProductCode;
        this.itemDescription = itemDescription;
        this.itemQuantity = itemQuantity;
        this.itemTotalAmount = itemTotalAmount;
        this.itemUnitCost = itemUnitCost;
    }

    public Item(Integer itemIndex, Integer sellerMcc, String sellerId, String sellerAddress, String sellerCity, String sellerState, String sellerCountry, String sellerCep, String sellerTaxId) {
        this.itemIndex = itemIndex;
        this.sellerMcc = sellerMcc;
        this.sellerId = sellerId;
        this.sellerAddress = sellerAddress;
        this.sellerCity = sellerCity;
        this.sellerState = sellerState;
        this.sellerCountry = sellerCountry;
        this.sellerCep = sellerCep;
        this.sellerTaxId = sellerTaxId;
    }

    public Item setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
        return this;
    }

    public Item setItemProductCode(String itemProductCode) {
        this.itemProductCode = itemProductCode;
        return this;
    }

    public Item setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public Item setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
        return this;
    }

    public Item setItemTotalAmount(Double itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
        return this;
    }

    public Item setItemUnitCost(Double itemUnitCost) {
        this.itemUnitCost = itemUnitCost;
        return this;
    }

    public Item setSellerMcc(Integer sellerMcc){
        this.sellerMcc = sellerMcc;
        return this;
    }

    public Item setSellerId(String sellerId){
        this.sellerId = sellerId;
        return this;
    }

    public Item setSellerAddress(String sellerAddress){
        this.sellerAddress = sellerAddress;
        return this;
    }

    public Item setSellerCity(String sellerCity){
        this.sellerCity = sellerCity;
        return this;
    }

    public Item setSellerState(String sellerState){
        this.sellerState = sellerState;
        return this;
    }
    
    public Item setSellerCountry(String sellerCountry){
        this.sellerCountry = sellerCountry;
        return this;
    }
    
    public Item setSellerCep(String sellerCep){
        this.sellerCep = sellerCep;
        return this;
    }
    
    public Item setSellerTaxId(String sellerTaxId){
        this.sellerTaxId = sellerTaxId;
        return this;
    }
}
