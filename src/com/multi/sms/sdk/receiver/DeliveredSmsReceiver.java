package com.multi.sms.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.multi.sms.sdk.core.SmsSendManager;

public class DeliveredSmsReceiver extends BroadcastReceiver {

	private static final String TAG = DeliveredSmsReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNum = intent.getStringExtra("phoneNum");
		String message = intent.getStringExtra("message");
		String sequence = intent.getStringExtra("sequence");
		String groupSequence = intent.getStringExtra("groupSequence");
		Log.e(TAG,"收信人已经成功接收   >>>   groupSequence:  "+groupSequence+ "   _____phoneNum:"+phoneNum+":"+message+":"+sequence);
		
		SmsSendManager.getDefault().notifyDelivered(groupSequence, sequence, true);

//		Toast.makeText(context, "收信人已经成功接收"+phoneNum, Toast.LENGTH_SHORT).show();
	}

}
