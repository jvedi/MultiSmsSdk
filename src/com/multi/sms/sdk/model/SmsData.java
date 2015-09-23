package com.multi.sms.sdk.model;

import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author qiang
 * 
 *         要发送的一条短信
 *
 */
public class SmsData implements Parcelable {

	public static final int SEND_STATUS_WAIT = -1;
	public static final int SEND_STATUS_SUCCESS = 0;
	public static final int SEND_STATUS_FAIL = 1;

	/** 此条短信的序列,如果有一条短信需要发送多次,那么就添加短信序列,以使其在短信发送组里多次存在 */
	private String mSequence = UUID.randomUUID().toString();

	/** 发送到的号码 */
	private String phoneNum;
	/** 发送的内容 */
	private String message;

	/** 发送成功的标识 -1 等待 ； 0 送达 ；1 失败 */
	private int sendStatus = SEND_STATUS_WAIT;
	/** 短信送达的标识 */
	private boolean deliveredStatus = false;
	
	/** 可以由用户自己定义的id*/
	private int sendCode;

	public SmsData() {

	}

	public SmsData(String phoneNum, String messgae) {
		this.phoneNum = phoneNum;
		this.message = messgae;
	}
	
	public SmsData(String phoneNum, String messgae,int id) {
		this.phoneNum = phoneNum;
		this.message = messgae;
		this.sendCode = id;
	}

	// public SmsData(String phoneNum, String messgae, String sequence) {
	// this.phoneNum = phoneNum;
	// this.message = messgae;
	// this.mSequence = sequence;
	// }

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getmSequence() {
		return mSequence;
	}

	// public void setmSequence(String mSequence) {
	// this.mSequence = mSequence;
	// }

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public boolean isDeliveredStatus() {
		return deliveredStatus;
	}

	public void setDeliveredStatus(boolean deliveredStatus) {
		this.deliveredStatus = deliveredStatus;
	}

	public boolean isSendSuccess() {
		return getSendStatus() == SEND_STATUS_SUCCESS;
	}

	public boolean isSendFail() {
		return getSendStatus() == SEND_STATUS_FAIL;
	}

	public boolean isSendWait() {
		return getSendStatus() == SEND_STATUS_WAIT;
	}

	public int getSendCode() {
		return sendCode;
	}

	public void setSendCode(int sendCode) {
		this.sendCode = sendCode;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mSequence);
		dest.writeString(phoneNum);
		dest.writeString(message);
		dest.writeInt(sendStatus);
		dest.writeInt(deliveredStatus ? 0 : 1); //true 0  false 1
		dest.writeInt(sendCode);
	}
	
	
	public static final Parcelable.Creator<SmsData> CREATOR = new Parcelable.Creator<SmsData>() {
		public SmsData createFromParcel(Parcel in) {
			return new SmsData(in);
		}

		public SmsData[] newArray(int size) {
			return new SmsData[size];
		}
	};

	private SmsData(Parcel in) {
		mSequence = in.readString();
		phoneNum = in.readString();
		message = in.readString();
		sendStatus = in.readInt();
		deliveredStatus = in.readInt() == 0 ? true : false;
		sendCode = in.readInt();
	}


}
