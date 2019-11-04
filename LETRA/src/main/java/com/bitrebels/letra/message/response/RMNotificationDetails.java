package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.Progress;

import java.time.LocalDate;
import java.util.Set;

public class RMNotificationDetails {

    long employeeId;

    String employeeName;

    LeaveRequest leaveRequest ;

    Set<Progress> progress;

    Set<LocalDate> dates;

    public RMNotificationDetails(LeaveRequest leaveRequest, Set<Progress> progress,long employeeId,
                                 String employeeName) {
        this.leaveRequest = leaveRequest;
        this.progress = progress;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }



    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public Set<Progress> getProgress() {
        return progress;
    }

    public void setProgress(Set<Progress> progress) {
        this.progress = progress;
    }

    public Set<LocalDate> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDate> dates) {
        this.dates = dates;
    }
}
