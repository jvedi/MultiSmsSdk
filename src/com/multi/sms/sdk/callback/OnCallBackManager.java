package com.multi.sms.sdk.callback;

import java.util.HashMap;
import java.util.Map;

public class OnCallBackManager {

	@SuppressWarnings("rawtypes")
	private Map<String,OnCallBackListener> callbacks = new HashMap<String, OnCallBackListener>();
	
	private static OnCallBackManager manager = new OnCallBackManager();
	
	private OnCallBackManager(){
		
	}
	
	public static OnCallBackManager getInstance(){
		return manager;
	}
	
	@SuppressWarnings("rawtypes")
	public void addCallBackListener(String mGroupSequece,OnCallBackListener callBackListener){
		callbacks.put(mGroupSequece, callBackListener);
	}
	
	@SuppressWarnings("rawtypes")
	public OnCallBackListener getCallBackListener(String mGroupSequece){
		return callbacks.get(mGroupSequece);
	}

}
