package com.multi.sms.sdk.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.multi.sms.sdk.core.SmsSendManager;
import com.multi.sms.sdk.model.SmsData;

public class SendSmsReceiver extends BroadcastReceiver {

	private static final String TAG = SendSmsReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNum = intent.getStringExtra("phoneNum");
		
		String message = intent.getStringExtra("message");
		String sequence = intent.getStringExtra("sequence");
		String groupSequence = intent.getStringExtra("groupSequence");
		
		switch (getResultCode()) {
		case Activity.RESULT_OK: 
			//短信发送成功
			SmsSendManager.getDefault().notifySented(groupSequence, sequence, SmsData.SEND_STATUS_SUCCESS);
//			Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
			Log.d(TAG,"短信发送成功 >>> groupSequence:  "+groupSequence+ "   _____phoneNum:"+phoneNum+":"+message+":"+sequence);

			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			//短信发送失败
			SmsSendManager.getDefault().notifySented(groupSequence, sequence, SmsData.SEND_STATUS_FAIL);
			Log.d(TAG,"短信发送失败 >>> groupSequence:  "+groupSequence+ "   _____phoneNum:"+phoneNum+":"+message+":"+sequence);

			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			break;
		}
		
		
	}

}
