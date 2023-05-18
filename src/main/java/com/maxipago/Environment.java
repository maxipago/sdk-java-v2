package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class Environment {
    private static final String SANDBOX = "https://testapi.maxipago.net";
    private static final String PRODUCTION = "https://api.maxipago.net";

    private String apiURL;

    private final String merchantId;
    private final String merchantKey;

    private Boolean transaction = true;
    private Boolean reports = false;
    private Boolean api = false;
    
    public Environment setAPI(Boolean API) {
        this.transaction = false;
        this.reports = false;
        this.api = API;
        return this;
    }

    public Environment setTransaction(Boolean transaction) {
        this.transaction = transaction;
        this.reports = !transaction;
        this.api = !transaction;
        return this;
    }

    public Environment setReports(Boolean reports) {
        this.reports = reports;
        this.transaction = !reports;
        this.api = !reports;
        return this;
    }

    public Environment(String merchantId, String merchantKey, Boolean production) {
        this.merchantId = merchantId;
        this.merchantKey = merchantKey;
        this.apiURL = Environment.PRODUCTION;

        if (!production) {
            this.apiURL = Environment.SANDBOX;
        }
    }
    
    public Environment(String merchantId, String merchantKey, String apiURL) {
        this.merchantId = merchantId;
        this.merchantKey = merchantKey;
        this.apiURL = apiURL;
    }

    public static Environment sandbox(String merchantId, String merchantKey) {
        return new Environment(merchantId, merchantKey, false);
    }

    public static Environment production(String merchantId, String merchantKey) {
        return new Environment(merchantId, merchantKey, true);
    }

    @XmlElement(name = "merchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @XmlElement(name = "merchantKey")
    public String getMerchantKey() {
        return merchantKey;
    }

    public String getEndpoint() {
        StringBuilder endPoint = new StringBuilder(apiURL);
    	
    	if (transaction) {
    		endPoint.append("/UniversalAPI/postXML");
        }

        if (reports) {
        	endPoint.append("/ReportsAPI/servlet/ReportsAPI");
        }
        
        if (api) {
        	endPoint.append("/UniversalAPI/postAPI");
        }

        return endPoint.toString();
    }
}
