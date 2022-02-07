package com.maxipago.paymentmethod.pix;

import java.util.ArrayList;

public class ExtraInfo {
    public ArrayList<Info> info;

    public ExtraInfo() {
        this.info = new ArrayList<>();
    }

    public ExtraInfo add(Info info) {
        this.info.add(info);
        return this;
    }
}
