package com.multi.sms.sdk.core;

import java.util.List;
import java.util.Random;

import com.multi.sms.sdk.model.SmsData;
import com.multi.sms.sdk.model.SmsDataGroup;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SmsSender {

	public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	
	private Context mContext;
	
	public SmsSender(Context context) {
		mContext = context;
	}
	
	/**
	 * 发送一组短信
	 * @param phoneNumber
	 * @param messages
	 */
	public void sendAGroupSms(SmsDataGroup aGroupSms){
		
		if(aGroupSms==null){
			return;
		}
		
		for(SmsData smsData : aGroupSms.getSmsDatas()){
			sendSMS(smsData,aGroupSms.getmGroupSequence());
		}
		
	}
	

//	/**
//	 * 直接调用短信接口发短信
//	 * 
//	 * @param phoneNumber
//	 * @param message
//	 */
//	public void sendSMS(String phoneNumber, String message) {
//		// 获取短信管理器
//		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
//		// 拆分短信内容（手机短信长度限制）
//		List<String> divideContents = smsManager.divideMessage(message);
//		for (String text : divideContents) {
//			//XXX 现有设计不会碰到这种拆分情况,一旦碰到这种情况,发送成功的计费回调必然变多.
//			smsManager.sendTextMessage(phoneNumber, null, text, getSendPI(mContext,phoneNumber,message),getDeliverPI(mContext,phoneNumber,message));
//		}
//	}
	
	/**
	 * 直接调用短信接口发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public void sendSMS(SmsData smsData,String groupSequence) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(smsData.getMessage());
		for (String text : divideContents) {
			//XXX 现有设计不会碰到这种拆分情况,一旦碰到这种情况,发送成功的计费回调必然变多.
			smsManager.sendTextMessage(smsData.getPhoneNum(), null, text, getSendPI(mContext,smsData,groupSequence),getDeliverPI(mContext,smsData,groupSequence));
		}
	}
	

	// 处理发送短信的返回状态
	public PendingIntent getSendPI(final Context context,SmsData smsData,String groupSequence) {
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		sentIntent.putExtra("phoneNum", smsData.getPhoneNum());
		sentIntent.putExtra("message", smsData.getMessage());
		sentIntent.putExtra("sequence", smsData.getmSequence());
		sentIntent.putExtra("groupSequence", groupSequence);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, new Random().nextInt(),sentIntent, 0);
		return sentPI;
	}

	public PendingIntent getDeliverPI(final Context context,SmsData smsData,String groupSequence) {
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverIntent.putExtra("phoneNum", smsData.getPhoneNum());
		deliverIntent.putExtra("message", smsData.getMessage());
		deliverIntent.putExtra("sequence", smsData.getmSequence());
		deliverIntent.putExtra("groupSequence", groupSequence);
		PendingIntent deliverPI = PendingIntent.getBroadcast(context, new Random().nextInt(),deliverIntent, 0);
		return deliverPI;
	}

}
