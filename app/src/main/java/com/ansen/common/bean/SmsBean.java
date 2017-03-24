package com.ansen.common.bean;

/**
 * Created by admin on 2016/8/5.
 */
public class SmsBean {

    private String mobileNo;
    private String messageTime;
    private String content;
    private int callType;

    public SmsBean() {
    }

    public SmsBean(String mobileNo, String messageTime, String content, int callType) {
        this.mobileNo = mobileNo;
        this.messageTime = messageTime;
        this.content = content;
        this.callType = callType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    @Override
    public String toString() {
        return "SmsBean{" +
                "mobileNo='" + mobileNo + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", content='" + content + '\'' +
                ", callType=" + callType +
                '}';
    }
}
