package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class Wallet {
	@XmlElement
    public String name;
	@XmlElement
	public SDWO sdwo;
	
	public Wallet setName(String name) {
        this.name = name;
        return this;
    }
	
	public Wallet setSDWO(SDWO sdwo) {
        this.sdwo = sdwo;
        return this;
    }
}
