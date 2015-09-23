package com.example.testplugin;

import java.util.Map;

import com.multi.sms.sdk.api.SmsPackage;
import com.multi.sms.sdk.api.SmsPayApi;
import com.multi.sms.sdk.callback.OnCallBackListener;
import com.multi.sms.sdk.model.SmsData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText phoneNum;
	private EditText message;
	private Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		message = (EditText) findViewById(R.id.message);
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new SendClick());
		SmsPayApi.init(getApplicationContext());
	}

	private class SendClick implements OnClickListener {

		private String TAG = SendClick.class.getSimpleName();

		@Override
		public void onClick(View v) {

			 SmsPackage smsPackage = new SmsPackage();
			 smsPackage.addSMS("10010", "1",0);
			 smsPackage.addSMS("10010", "2",1);
			 
			SmsPayApi.getInstance().startPay(smsPackage, new OnCallBackListener<SmsData>() {
				@Override
				public void onSendFinish(int status, String msg) {
					Toast.makeText(getApplicationContext(), status + ":" + msg, Toast.LENGTH_SHORT).show();
					Log.e("result", status + ":" + msg);

				}

				@Override
				public void sendDetailResult(Map<String, SmsData> success, Map<String, SmsData> fail, Map<String, SmsData> wait) {
					Log.e(TAG, "sendDetailResult:" + "success " + success.size() + "   ------   fail " + fail.size() + "  -----  wait " + wait.size());
					for (SmsData smsData : success.values()) {
						Log.e(TAG, "success   ----  " + smsData.getPhoneNum() + ":" + smsData.getMessage());
					}
					for (SmsData smsData : fail.values()) {
						Log.e(TAG, "fail   ----  " + smsData.getPhoneNum() + ":" + smsData.getMessage());
					}
					for (SmsData smsData : wait.values()) {
						Log.e(TAG, "wait   ----  " + smsData.getPhoneNum() + ":" + smsData.getMessage());
					}
				}

				@Override
				public void onOneSend(int status, int sendCode, Bundle data) {
					Log.e(TAG, "onOneSend  >>>  "+status+":"+sendCode+" ----- "+data.getString(OnCallBackListener.SEND_NUM)+">>>"+data.getString(OnCallBackListener.SEND_MESSAGE));
					
				}

				@Override
				public void onOneDelivered(boolean status, int sendCode, Bundle data) {
					Log.e(TAG, "onOneDelivered  >>>  "+status+":"+sendCode+" ----- "+data.getString(OnCallBackListener.SEND_NUM)+">>>"+data.getString(OnCallBackListener.SEND_MESSAGE));
				}

			});

		}
	}

}
