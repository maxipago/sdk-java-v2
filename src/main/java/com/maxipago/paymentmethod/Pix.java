package com.maxipago.paymentmethod;

import com.maxipago.paymentmethod.pix.ExtraInfo;
import com.maxipago.paymentmethod.pix.Info;

import java.util.ArrayList;

public class Pix {
    public Integer expirationTime;
    public String paymentInfo;
    public ExtraInfo extraInfo;

    public Pix() {
        extraInfo = new ExtraInfo();
    }

    public Pix addInfo(String name, String value) {
        extraInfo.add(new Info(name, value));
        return this;
    }

    public Pix setExpirationTime(Integer expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public Pix setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
        return this;
    }
}
