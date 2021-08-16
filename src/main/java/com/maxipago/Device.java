package com.maxipago;

import javax.xml.bind.annotation.XmlElement;

public class Device {

	public String deviceType3ds;
    public String colorDepth;
    public boolean javaEnabled;
    public String language;
    public String screenHeight;
    public String screenWidth;
    public int timeZoneOffset;
    public String acceptHeader;
    public String ip;
    public String sdkAppId;
    public String sdkEncData;
    public String sdkEphemeralPubKey;
    public String sdkMaxTimeout;
    public String sdkReferenceNumber;
    public String sdkTransId;
    public String userAgent;
    
    @XmlElement(name = "renderOptions")
    public RenderOptions renderOptions;
    
    public Device setDeviceType3ds(String deviceType3ds) {
        this.deviceType3ds = deviceType3ds;
        return this;
    }


	public Device setColorDepth(String colorDepth) {
		this.colorDepth = colorDepth;
		return this;
	}


	public Device setJavaEnabled(boolean javaEnabled) {
		this.javaEnabled = javaEnabled;
		return this;
	}


	public Device setLanguage(String language) {
		this.language = language;
		return this;
	}

	public Device setScreenHeight(String screenHeight) {
		this.screenHeight = screenHeight;
		return this;
	}

	public Device setScreenWidth(String screenWidth) {
		this.screenWidth = screenWidth;
		return this;
	}

	public Device setTimeZoneOffset(int timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
		return this;
	}

	public Device setAcceptHeader(String acceptHeader) {
		this.acceptHeader = acceptHeader;
		return this;
	}


	public Device setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Device setSdkAppId(String sdkAppId) {
		this.sdkAppId = sdkAppId;
		return this;
	}

	public Device setSdkEncData(String sdkEncData) {
		this.sdkEncData = sdkEncData;
		return this;
	}


	public Device setSdkEphemeralPubKey(String sdkEphemeralPubKey) {
		this.sdkEphemeralPubKey = sdkEphemeralPubKey;
		return this;
	}


	public Device setSdkMaxTimeout(String sdkMaxTimeout) {
		this.sdkMaxTimeout = sdkMaxTimeout;
		return this;
	}


	public Device setSdkReferenceNumber(String sdkReferenceNumber) {
		this.sdkReferenceNumber = sdkReferenceNumber;
		return this;
	}


	public Device setSdkTransId(String sdkTransId) {
		this.sdkTransId = sdkTransId;
		return this;
	}


	public Device setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}


	public Device setRenderOptions(RenderOptions renderOptions) {
		this.renderOptions = renderOptions;
		return this;
	}
}
