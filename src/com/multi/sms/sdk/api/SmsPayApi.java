package com.multi.sms.sdk.api;

import android.content.Context;
import android.os.Bundle;

import com.multi.sms.sdk.callback.OnCallBackListener;
import com.multi.sms.sdk.callback.OnCallBackManager;
import com.multi.sms.sdk.core.SmsSendManager;
import com.multi.sms.sdk.model.SmsData;
import com.multi.sms.sdk.model.SmsDataGroup;

public class SmsPayApi {

	private static SmsPayApi instance = null;
	private static Context mContext;
	
	private SmsPayApi(){
		
	}
	
	public static void init(Context context){
		if(instance==null){
			mContext = context;
			instance = new SmsPayApi();
			SmsSendManager.init(mContext);
		}
	}
	
	public static SmsPayApi getInstance(){
		return instance;
	}
	
//	public void startPay(Bundle bundle,OnCallBackListener<SmsData> callBackListener){
//
//		SmsDataGroup smsGroup = testBuild();
//		//注册监听、发送短信
//		OnCallBackManager.getInstance().addCallBackListener(smsGroup.getmGroupSequence(), callBackListener);
//		SmsSendManager.getDefault().sendSmsGroup(smsGroup);
//	}
//	
	public void startPay(SmsPackage smsPackage,OnCallBackListener<SmsData> callBackListener){
		SmsDataGroup smsGroup = smsPackage.getPackage();
		//注册监听、发送短信
		OnCallBackManager.getInstance().addCallBackListener(smsGroup.getmGroupSequence(), callBackListener);
		SmsSendManager.getDefault().sendSmsGroup(smsGroup);
	}
	
	
	public SmsDataGroup buildSmsGroup(Bundle bundle){
		//从服务端获取支付代码
		int money = bundle.getInt("money");
		String productName = bundle.getString("productName");
		
		return testBuild();
		
	}

	
	private SmsDataGroup testBuild(){
		
		SmsDataGroup smsGroup = new SmsDataGroup();
		smsGroup.addSmsData(new SmsData("18357052197","多条测试1"));
		smsGroup.addSmsData(new SmsData("10010","3"));
		smsGroup.addSmsData(new SmsData("18357052197","多条测试2"));
		return smsGroup;
	}
	
	
	
	
}
