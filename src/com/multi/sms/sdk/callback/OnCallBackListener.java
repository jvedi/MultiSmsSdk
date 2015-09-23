package com.multi.sms.sdk.callback;

import java.util.Map;

import android.os.Bundle;

public interface OnCallBackListener<T> {

		public static final String SEND_NUM = "SendPhoneNumber";
		public static final String SEND_MESSAGE = "sendMessage";
	
	    void onSendFinish(int status,String msg);
	
	    /**
	     * @param success  发送成功的
	     * @param fail  发送失败的
	     * @param wait 尚未发送的
	     */
	    void sendDetailResult(Map<String,T> success,Map<String,T> fail , Map<String,T> wait);
	    
	    /**
	     * 每条短信发送成功都会调用
	     * @param status  发送的结果状态
	     * @param sendCode 发送码
	     * @param data 包含 phoneNum , message 内容 . key 分别为 SEND_NUM 、SEND_MESSAGE
	     */
	    void onOneSend(int status,int sendCode,Bundle data);
	    
	    /**
	     *　暂时未启用
	     * @param status
	     * @param msg
	     */
//	    void onDelivered(int status,String msg);

	    /**
	     * @param success 送达成功的数据
	     * @param unkownDelivered 并不代表送达失败的，只是没有获取到送达通知的
	     */
//	    void deliveredDetailResult(Map<String,T> success,Map<String,T> unkownDelivered);
	    
	    /**
	     * 每条短信成功送达都会调用
	     * @param status  发送的结果状态  true:送达  false:未收到送达通知
	     * @param sendCode 发送码
	     * @param data 包含 phoneNum , message 内容 . key 分别为 SEND_NUM 、SEND_MESSAGE
	     */
	    void onOneDelivered(boolean status,int sendCode,Bundle data);
	    
	    
	    
}
