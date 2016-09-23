package com.tomorrow_p.common.bean;

/**
 * Created by admin on 2016/8/5.
 */
public class CallLogBean {
    private String name;
    private String mobileNo;
    private String callTime;
    private String callDuration;
    private int callType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    @Override
    public String toString() {
        return "CallLogBean{" +
                "name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", callTime='" + callTime + '\'' +
                ", callDuration='" + callDuration + '\'' +
                ", callType='" + callType + '\'' +
                '}';
    }
}
