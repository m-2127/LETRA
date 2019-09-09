package com.bitrebels.letra.message.request;

import java.util.ArrayList;
import java.util.List;

public class LeaveResponse {

    private String leaveType;

    private long employeeID;

    private String description;

    private boolean approval;

//    @JsonDeserialize(using = DateHandler.class)
    private List<String> dates = new ArrayList<>();

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
