package com.maxipago.paymentmethod;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "payType")
@XmlSeeAlso({OnlineDebit.class, Boleto.class, Card.class})
public class PayType {
    public Card creditCard;
    public Card debitCard;
    public Boleto boleto;
    public OnlineDebit onlineDebit;
    public Token onFile;
    public Pix pix;

    public PayType setToken(Token onFile) {
        this.onFile = onFile;

        return this;
    }

    public PayType setDebitCard(Card debitCard) {
        this.debitCard = debitCard;

        return this;
    }

    public PayType setCreditCard(Card creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public PayType setBoleto(Boleto boleto) {
        this.boleto = boleto;
        return this;
    }

    public PayType setOnlineDebit(OnlineDebit onlineDebit) {
        this.onlineDebit = onlineDebit;
        return this;
    }

    public PayType setPix(Pix pix) {
        this.pix = pix;
        return this;
    }
}
