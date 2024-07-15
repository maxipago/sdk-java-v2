package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

import com.maxipago.enums.BusinessApplicationIdentifier;
import com.maxipago.enums.SDWOProcessingType;

public class SDWO {
	@XmlElement
    public int id;
	@XmlElement
    public String processingType;
	@XmlElement
    public String senderTaxIdentification;
	@XmlElement
    public String businessApplicationIdentifier;
	@XmlElement
    public String paymentDestination;
	@XmlElement
    public String merchantTaxId;
	@XmlElement
    public ReceiverData receiverData;
	
	
	public SDWO setId(int id) {
		this.id = id;
		return this;
	}
	
	public SDWO setProcessingType(SDWOProcessingType processingType) {
		this.processingType = processingType.toString();
		return this;
	}
	
	public SDWO setSenderTaxIdentification(String senderTaxIdentification) {
		this.senderTaxIdentification = senderTaxIdentification;
		return this;
	}
	
	public SDWO setBusinessApplicationIdentifier(BusinessApplicationIdentifier businessApplicationIdentifier) {
		this.businessApplicationIdentifier = businessApplicationIdentifier.toString();
		return this;
	}

	public SDWO setReceiverData(ReceiverData receiverData) {
		this.receiverData = receiverData;
		return this;
	}
}
