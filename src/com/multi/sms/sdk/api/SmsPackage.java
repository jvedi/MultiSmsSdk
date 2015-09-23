package com.multi.sms.sdk.api;

import com.multi.sms.sdk.model.SmsData;
import com.multi.sms.sdk.model.SmsDataGroup;

public class SmsPackage {

	private SmsDataGroup smsGroup;
	
	public SmsPackage() {
		  smsGroup = new SmsDataGroup();
	}
	
	public void addSMS(String phoneNum,String message,int sendCode){
		smsGroup.addSmsData(new SmsData(phoneNum,message,sendCode));
	}
	
	protected SmsDataGroup getPackage(){
		return smsGroup;
	}
	
}
