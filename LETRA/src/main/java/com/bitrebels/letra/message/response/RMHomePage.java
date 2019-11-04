package com.bitrebels.letra.message.response;

public class RMHomePage {

    long leaveReqId;

    String leaveType;

    String name;

    public RMHomePage(long leaveReqId, String leaveType, String name) {
        this.leaveReqId = leaveReqId;
        this.leaveType = leaveType;
        this.name = name;
    }

    public long getLeaveReqId() {
        return leaveReqId;
    }

    public void setLeaveReqId(long leaveReqId) {
        this.leaveReqId = leaveReqId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
