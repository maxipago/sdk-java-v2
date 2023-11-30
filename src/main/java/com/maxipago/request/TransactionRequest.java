package com.maxipago.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.maxipago.Environment;
import com.maxipago.Order;

@XmlRootElement(name = "transaction-request")
public class TransactionRequest extends AbstractRequest<TransactionRequest, TransactionResponse> {
    @XmlElement
    public String version = "3.1.1.15";

    private Order order;

    public TransactionRequest() {}

    public TransactionRequest(Environment environment) {
        super(environment);
        order = new Order();
    }

    public Order getOrder() {
        return order;
    }

    public TransactionRequest setOrder(Order order) {
        this.order = order;
        return this;
    }

    protected Class<TransactionRequest> getContext() {
        return TransactionRequest.class;
    }
    protected TransactionResponse getResponseObject() {
        return new TransactionResponse();
    }
}
