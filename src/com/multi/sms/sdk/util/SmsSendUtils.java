package com.multi.sms.sdk.util;

import com.multi.sms.sdk.model.SmsData;
import com.multi.sms.sdk.model.SmsDataGroup;

public class SmsSendUtils {
	
	/**
	 * @return 是否已经全部发送完毕
	 */
	public boolean isAllSended(SmsDataGroup smsGroup) {
		if (smsGroup.getSize() <= 0) {
			return false;
		}
		int wait = 0;

		for (SmsData smsData : smsGroup.getSmsDatas()) {
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
	public boolean isSuccess(SmsDataGroup smsGroup) {
		if (smsGroup.getSize() <= 0) {
			return false;
		}
		// 所有的短信都发送完了，只有成功和失败两种状态了
		int success = 0;
		int fail = 0;
		int wait = 0;

		for (SmsData smsData : smsGroup.getSmsDatas()) {
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
		if ((success + fail == smsGroup.getSize()) && (success > fail)) {
			return true;
		}
		return false;

	}

		
	

}
