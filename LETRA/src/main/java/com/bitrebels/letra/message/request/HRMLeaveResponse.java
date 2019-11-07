package com.bitrebels.letra.message.request;

public class HRMLeaveResponse {

    private long leaveReqId;

    private long employeeID;

    private String description;

    private boolean approval;

    public long getLeaveReqId() {
        return leaveReqId;
    }

    public void setLeaveReqId(long leaveReqId) {
        this.leaveReqId = leaveReqId;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
