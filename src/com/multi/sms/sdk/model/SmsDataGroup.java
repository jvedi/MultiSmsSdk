package com.multi.sms.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author qiang
 * 
 *         要发送的一组短信
 *
 */
public class SmsDataGroup {

	/** 存储要发送的一组短信 */
	private List<SmsData> smsGroup = new ArrayList<SmsData>();

	private String mGroupSequence = UUID.randomUUID().toString();

	public void addSmsData(SmsData smsData) {
		smsGroup.add(smsData);
	}

	/**
	 * @return 获取此短信组内一共有多少条短信需要发送
	 */
	public int getSize() {
		return smsGroup.size();
	}

	/**
	 * 查询短信对象是否已经被添加进来
	 * 
	 * @param smsData
	 * @return
	 */
	public boolean contains(SmsData smsData) {
		return smsGroup.contains(smsData);
	}

	/**
	 * 获取短信组里面存在的短信数据体,一般用来在短信发送的时候,逐条发送
	 * 
	 * @return
	 */
	public List<SmsData> getSmsDatas() {
		return smsGroup;
	}

	public String getmGroupSequence() {
		return mGroupSequence;
	}

	
	
	
	/**
	 * @return 是否已经全部发送完毕
	 */
	public boolean isAllSended() {
		if (smsGroup.size() <= 0) {
			return false;
		}
		int wait = 0;

		for (SmsData smsData : smsGroup) {
			switch (smsData.getSendStatus()) {
			case SmsData.SEND_STATUS_WAIT:
				wait = wait + 1;
				break;

			default:
				break;
			}
		}
		return wait == 0;
	}

	/**
	 * 本地成功的判断标准（有相应接口的还需要访问服务器）
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		if (smsGroup.size() <= 0) {
			return false;
		}
		// 所有的短信都发送完了，只有成功和失败两种状态了
		int success = 0;
		int fail = 0;
		int wait = 0;

		for (SmsData smsData : smsGroup) {
			switch (smsData.getSendStatus()) {
			case SmsData.SEND_STATUS_SUCCESS:
				success = success + 1;
				break;
			case SmsData.SEND_STATUS_FAIL:
				fail = fail + 1;
				break;
			case SmsData.SEND_STATUS_WAIT:
				wait = wait + 1;
				break;

			default:
				break;
			}
		}
		// 有未发送完成的，视为尚未成功
		if (wait > 0) {
			return false;
		}
		// TODO 注意！！！这里是成功的规则定义。默认全部发送完成，并且成功条数大于失败条数，视为成功。此规则可根据敲定的成功规则修改
		if ((success + fail == smsGroup.size()) && (success > fail)) {
			return true;
		}
		return false;

	}
	
	/**
	 * 查询短信发送状态
	 * @param queryStatus
	 * @return
	 */
	public Map<String, SmsData> querySendStatus(int queryStatus) {
		
		Map<String,SmsData> waitMap = new HashMap<String, SmsData>();
		Map<String,SmsData> successMap = new HashMap<String, SmsData>();
		Map<String,SmsData> failMap = new HashMap<String, SmsData>();
		
		if (smsGroup.size() <= 0) {
			return waitMap;
		}

		for (SmsData smsData : smsGroup) {
			switch (smsData.getSendStatus()) {
			case SmsData.SEND_STATUS_WAIT:
				waitMap.put(smsData.getmSequence(), smsData);
				break;
			case SmsData.SEND_STATUS_SUCCESS:
				successMap.put(smsData.getmSequence(), smsData);
				break;
			case SmsData.SEND_STATUS_FAIL:
				failMap.put(smsData.getmSequence(), smsData);
				break;
			default:
				break;
			}
		}
		if(queryStatus==SmsData.SEND_STATUS_WAIT){
			return waitMap;
		}
		if(queryStatus==SmsData.SEND_STATUS_SUCCESS){
			return successMap;
		}
		if(queryStatus==SmsData.SEND_STATUS_FAIL){
			return failMap;
		}
		return null;
	}
	
	
	
	/**
	 * 查询送达状态
	 * @param flag true:送达的   false:未送达的
	 * @return
	 */
	public Map<String, SmsData> queryDeliveredStatus(boolean flag) {
		
		Map<String,SmsData> successMap = new HashMap<String, SmsData>();
		Map<String,SmsData> failMap = new HashMap<String, SmsData>();
		
		if (smsGroup.size() <= 0) {
			return successMap;
		}

		for (SmsData smsData : smsGroup) {
			if(smsData.isDeliveredStatus()){
				successMap.put(smsData.getmSequence(), smsData);
			}else{
				failMap.put(smsData.getmSequence(), smsData);
			}
		}
		if(flag){
			return successMap;
		}
		return failMap;
	}
	

}
