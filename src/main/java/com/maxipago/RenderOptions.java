package com.maxipago;

public class RenderOptions {

	public String sdkInterface;
    public String sdkUiType;
	
	public RenderOptions setSdkInterface(String sdkInterface) {

    	this.sdkInterface = sdkInterface;
        return this;
    }
	
	public RenderOptions setSdkUiType(String sdkUiType) {
    	
        this.sdkUiType = sdkUiType;
        return this;
    }
}
