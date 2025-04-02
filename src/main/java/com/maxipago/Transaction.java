package com.maxipago;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.maxipago.enums.ChallengePreference;
import com.maxipago.paymentmethod.Boleto;
import com.maxipago.paymentmethod.Card;
import com.maxipago.paymentmethod.OnlineDebit;
import com.maxipago.paymentmethod.PayType;
import com.maxipago.paymentmethod.Pix;
import com.maxipago.paymentmethod.Token;

@XmlSeeAlso({PayType.class, FraudDetails.class})
public class Transaction {
    @XmlElement(name = "transactionID")
    public String transactionId;

    @XmlElement(name = "orderID")
    public String orderId;

    @XmlElement(name = "processorID")
    public String processorId = Card.REDE;

    @XmlElement(name = "referenceNum")
    public String referenceNum;

    @XmlElement(name = "fraudCheck")
    public String fraudCheck = "N";

    @XmlElement(name = "ipAddress")
    public String ipAddress;

    @XmlElement
    public String invoiceNumber;

    @XmlElement(name = "customerIdExt")
    public String customerIdExt;

    @XmlElement(name = "billing")
    public Customer billing;

    @XmlElement(name = "shipping")
    public Customer shipping;

    @XmlElement(name = "transactionDetail")
    public TransactionDetail transactionDetail;

    @XmlElement(name = "payment")
    public Payment payment;

    @XmlElement(name = "saveOnFile")
    public SaveOnFile saveOnFile;

    @XmlElement
    public ItemList itemList;

    @XmlElement
    public String userAgent;

    @XmlElement
    public Authentication authentication;

    @XmlElement
    public Recurring recurring;

    @XmlElement
    public FraudDetails fraudDetails;
    
    @XmlElement
    public Device device;
    
    @XmlElement
    public Wallet wallet;

    @XmlElement
    public Integer paymentFacilitatorID;

    public Transaction fraudDetails(String fraudProcessorID, String fraudToken) {
        return fraudDetails(fraudProcessorID, fraudToken, "N", "N");
    }

    public Transaction fraudDetails(String fraudProcessorID, String fraudToken, String captureOnLowRisk, String voidOnHighRisk) {
        setFraudCheck("Y");

        this.fraudDetails = new FraudDetails();
        this.fraudDetails.fraudProcessorID = fraudProcessorID;
        this.fraudDetails.fraudToken = fraudToken;
        this.fraudDetails.captureOnLowRisk = captureOnLowRisk;
        this.fraudDetails.voidOnHighRisk = voidOnHighRisk;

        return this;
    }

    public Transaction saveOnFile(String customerToken, String onFileEndDate) {
        this.saveOnFile = new SaveOnFile(customerToken, onFileEndDate);

        return this;
    }

    public Transaction addItem(Integer itemIndex, String itemProductCode, String itemDescription, Integer itemQuantity, Double itemTotalAmount, Double itemUnitCost) {
        if (itemList == null) {
            itemList = new ItemList();
        }

        itemList.item.add((new Item(itemIndex, itemProductCode, itemDescription, itemQuantity, itemTotalAmount, itemUnitCost)));
        itemList.itemCount++;

        return this;
    }

    public Transaction addItem(Integer itemIndex, String sellerMcc, String sellerId, String sellerAddress, String sellerCity, String sellerState, String sellerCountry, String sellerCep, String sellerTaxId) {
        if (itemList == null) {
            itemList = new ItemList();
        }

        itemList.item.add((new Item(itemIndex, sellerMcc, sellerId, sellerAddress, sellerCity, sellerState, sellerCountry, sellerCep, sellerTaxId)));
        itemList.itemCount++;

        return this;
    }
    
    public Transaction addItem(Integer itemIndex, String sellerMcc, String sellerId, String sellerAddress, String sellerCity, String sellerState, String sellerCountry, String sellerCep, String sellerTaxId, String sellerTaxIdName) {
        if (itemList == null) {
            itemList = new ItemList();
        }

        itemList.item.add((new Item(itemIndex, sellerMcc, sellerId, sellerAddress, sellerCity, sellerState, sellerCountry, sellerCep, sellerTaxId, sellerTaxIdName)));
        itemList.itemCount++;

        return this;
    }

    public Transaction setAuthentication(String mpiProcessorID, String onFailure) {
        this.authentication = new Authentication(mpiProcessorID, onFailure);
        return this;
    }
    
    public Transaction setAuthentication(String mpiProcessorID, String onFailure, ChallengePreference challengePreference) {
        this.authentication = new Authentication(mpiProcessorID, onFailure, challengePreference);
        return this;
    }
    
    public Transaction setAuthentication(String mpiProcessorID, String onFailure, ChallengePreference challengePreference, String responseMode, String sendNotification) {
        this.authentication = new Authentication(mpiProcessorID, onFailure, challengePreference,responseMode, sendNotification);
        return this;
    }

    public Transaction setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public Transaction setCustomerIdExt(String customerIdExt) {
        this.customerIdExt = customerIdExt;
        return this;
    }

    public Transaction setProcessorId(String processorId) {
        this.processorId = processorId;
        return this;
    }

    public Transaction setFraudCheck(String fraudCheck) {
        this.fraudCheck = fraudCheck;
        return this;
    }

    public Transaction setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
        return this;
    }

    public Transaction billingAndShipping(Customer customer) {
        setBilling(customer);
        setShipping(customer);

        return this;
    }

    public Transaction setBilling(Customer billing) {
        this.billing = billing;
        
        return this;
    }

    public Transaction setShipping(Customer shipping) {
        this.shipping = shipping;
        return this;
    }
    
    public Transaction device(Device device) {
    	this.device = device;
    	return this;		
    }

    public Transaction setToken(Token token) {
        PayType payType = new PayType();
        payType.onFile = token;

        return setPayType(payType);
    }

    public Transaction setPix(Pix pix) {
        PayType payType = new PayType();
        payType.pix = pix;

        return setPayType(payType);
    }

    public Transaction setCreditCard(Card creditCard) {
        PayType payType = new PayType();
        payType.creditCard = creditCard;

        return setPayType(payType);
    }

    public Transaction setDebitCard(Card debitCard) {
        PayType payType = new PayType();
        payType.debitCard = debitCard;

        return setPayType(payType);
    }

    public Transaction setBoleto(Boleto boleto) {
        PayType payType = new PayType();
        payType.boleto = boleto;

        return setPayType(payType);
    }

    public Transaction setOnlineDebit(OnlineDebit onlineDebit) {
        PayType payType = new PayType();
        payType.onlineDebit = onlineDebit;

        return setPayType(payType);
    }

    public Transaction setPayType(PayType payType) {
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setPayType(payType);

        setTransactionDetail(transactionDetail);

        return this;
    }

    public Transaction setTransactionDetail(TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
        return this;
    }

    public Transaction setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Transaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Transaction setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Transaction setRecurring(Recurring recurring) {
        this.recurring = recurring;
        return this;
    }
    
    public Transaction setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    
    public Transaction setWallet(Wallet wallet) {
    	this.wallet = wallet;
    	return this;
    }

    public Transaction setPaymentFacilitatorID(Integer paymentFacilitatorID){
        this.paymentFacilitatorID = paymentFacilitatorID;
        return this;
    }
}
