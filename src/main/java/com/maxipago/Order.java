package com.maxipago;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class Order {
    @XmlElement
    private Transaction auth;

    @XmlElement
    private Transaction fraud;

    @XmlElement
    private Transaction capture;

    @XmlElement
    private Transaction sale;

    @XmlElement(name = "void")
    private Transaction cancel;

    @XmlElement(name = "return")
    private Transaction reversal;

    @XmlElement(name = "pixReturn")
    public Transaction pixReturn;

    @XmlElement
    private Transaction zeroDollar;

    @XmlElement
    private Transaction recurringPayment;

    private void clean() {
        auth = null;
        fraud = null;
        capture = null;
        sale = null;
        cancel = null;
        reversal = null;
        recurringPayment = null;
        zeroDollar = null;
    }

    public Transaction createAuth() {
        clean();

        auth = new Transaction();

        return auth;
    }

    public Transaction createSale() {
        clean();

        sale = new Transaction();

        return sale;
    }

    public Transaction createCapture() {
        clean();

        capture = new Transaction();
        capture.setProcessorId(null);
        capture.setFraudCheck(null);

        return capture;
    }

    public Transaction createVoid() {
        cancel = new Transaction();
        cancel.setProcessorId(null);
        cancel.setFraudCheck(null);

        return cancel;
    }

    public Transaction createPixRefund() {
        reversal = new Transaction();
        reversal.setProcessorId(null);
        reversal.setFraudCheck(null);

        return reversal;
    }

    public Transaction createRefund() {
        reversal = new Transaction();
        reversal.setProcessorId(null);
        reversal.setFraudCheck(null);

        return reversal;
    }

    public Transaction createRecurringPayment() {
        recurringPayment = new Transaction();
        recurringPayment.setFraudCheck(null);

        return recurringPayment;
    }

    public Transaction createZeroDollar() {
        zeroDollar = new Transaction();
        zeroDollar.setFraudCheck(null);

        return zeroDollar;
    }
}
