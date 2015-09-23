package com.multi.sms.sdk.core;

import java.util.HashMap;
import java.util.Map;

import com.multi.sms.sdk.callback.OnCallBackListener;
import com.multi.sms.sdk.callback.OnCallBackManager;
import com.multi.sms.sdk.model.SmsData;
import com.multi.sms.sdk.model.SmsDataGroup;
import com.multi.sms.sdk.receiver.DeliveredSmsReceiver;
import com.multi.sms.sdk.receiver.SendSmsReceiver;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * @author qiang
 *  本类用来记录管理要发送的短信,和回调的次数.
 *  发送短信的组命令,会优先由这里分配,并注册记录,
 *  每一次 短信发送成功/短信发送到达 ,都会到这里(调用)汇报,对比已经成功的条数,只有当注册记录要发送的条数,全部都成功了,才能记录为成功.
 *  
 */
public class SmsSendManager {

	private static final String TAG = SmsSendManager.class.getSimpleName();
	private static SmsSendManager mDefaultInstance;
	private static Context mContext;
	private SmsSender mSender;
	
	/** 等待发送的短信组*/
	private static Map<String,SmsDataGroup> registerSendData = new HashMap<String, SmsDataGroup>();
	  
	
	private SmsSendManager(){
		mContext.registerReceiver(new SendSmsReceiver(), new IntentFilter(SmsSender.SENT_SMS_ACTION));
		mContext.registerReceiver(new DeliveredSmsReceiver(), new IntentFilter(SmsSender.DELIVERED_SMS_ACTION));
		mSender = new SmsSender(mContext);
	}
	
	public static void init(Context context){
		if(mDefaultInstance == null){
			mContext = context;
			mDefaultInstance = new SmsSendManager();
		}
	}
	
	public static SmsSendManager getDefault(){
		return mDefaultInstance;
	}
	
	/**
	 * 发送短信组,并注册 ( //TODO 应当考虑添加校验机制，防止重复添加或者覆盖 )
	 */
	public void sendSmsGroup(SmsDataGroup aGroupSms){
		registSmsGroup(aGroupSms);
		realSendSmsGroup(aGroupSms);
	}
	
	private void realSendSmsGroup(SmsDataGroup aGroupSms){
		mSender.sendAGroupSms(aGroupSms);
	}
	
	private void registSmsGroup(SmsDataGroup aGroupSms){
		registerSendData.put(aGroupSms.getmGroupSequence(), aGroupSms);
	}
	
	/**
	 * 唤醒查询是否已经发送完毕
	 * @param groupSequence 短信组序列
	 * @param sequence  短信序列
	 */
	@SuppressWarnings("unchecked")
	public void notifySented(String groupSequence,String sequence,int sendStatus){
		try {
			SmsDataGroup smsDataGroup = registerSendData.get(groupSequence);
			if(smsDataGroup == null){
				return;
			}

			for(SmsData smsData : smsDataGroup.getSmsDatas()){
					if(sequence.equals(smsData.getmSequence())){
						smsData.setSendStatus(sendStatus);
						Bundle data = new Bundle();
						data.putString(OnCallBackListener.SEND_NUM, smsData.getPhoneNum());
						data.putString(OnCallBackListener.SEND_MESSAGE, smsData.getMessage());
						OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onOneSend(smsData.getSendStatus(), smsData.getSendCode(), data);
					}
			}
			
			if(!smsDataGroup.isAllSended()){
				return;
			}
			if(smsDataGroup.isSuccess()){
				//TODO 短信发送成功了，下发道具回调，（有必要的情况下通知服务器）
				OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onSendFinish(SmsData.SEND_STATUS_SUCCESS, "发送成功");
			}else{
				//TODO 短信发送失败了
				OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onSendFinish(SmsData.SEND_STATUS_FAIL, "发送失败");
			}
			//回调详情
			OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).
			sendDetailResult(smsDataGroup.querySendStatus(SmsData.SEND_STATUS_SUCCESS), smsDataGroup.querySendStatus(SmsData.SEND_STATUS_FAIL), smsDataGroup.querySendStatus(SmsData.SEND_STATUS_WAIT));
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		
	}
	
	
	/**
	 * 唤醒查询是否已经全部发送到达
	 * @param groupSequence 短信组序列
	 * @param sequence  短信序列
	 */
	public void notifyDelivered(String groupSequence,String sequence,boolean deliveredStatus){
		try {
			SmsDataGroup smsDataGroup = registerSendData.get(groupSequence);
			if(smsDataGroup == null){
				return;
			}

			for(SmsData smsData : smsDataGroup.getSmsDatas()){
					if(sequence.equals(smsData.getmSequence())){
						smsData.setDeliveredStatus(deliveredStatus);
						Bundle data = new Bundle();
						data.putString(OnCallBackListener.SEND_NUM, smsData.getPhoneNum());
						data.putString(OnCallBackListener.SEND_MESSAGE, smsData.getMessage());
						OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onOneDelivered(smsData.isDeliveredStatus(), smsData.getSendCode(), data);
					}
			}
			
			//只有全部发送完成，才可以响应回调发送完成结果，到达完成结果
//			if(!smsDataGroup.isAllSended()){
//				return;
//			}
//			if(smsDataGroup.isSuccess()){
//				//TODO 短信发送成功了，下发道具回调，（有必要的情况下通知服务器）
//				OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onDelivered(SmsData.SEND_STATUS_SUCCESS, "发送成功");
//			}else{
//				//TODO 短信发送失败了
//				OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).onDelivered(SmsData.SEND_STATUS_FAIL, "发送失败");
//			}
			//送达的详情 
//			OnCallBackManager.getInstance().getCallBackListener(smsDataGroup.getmGroupSequence()).deliveredDetailResult(smsDataGroup.queryDeliveredStatus(true), smsDataGroup.queryDeliveredStatus(false));
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		
	}
	
	
	
}
