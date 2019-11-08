package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.Progress;

import java.time.LocalDate;
import java.util.Set;

public class HRMNotificationDetails {

    long employeeId;

    long leaveReqId;

    String employeeName;

    String leaveType;

    LocalDate leaveStart;

    LocalDate leaveEnd;

    String status;

    public HRMNotificationDetails(long employeeId, long leaveReqId, String employeeName, String leaveType, LocalDate leaveStart, LocalDate leaveEnd, String status) {
        this.employeeId = employeeId;
        this.leaveReqId = leaveReqId;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.leaveStart = leaveStart;
        this.leaveEnd = leaveEnd;
        this.status = status;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getLeaveReqId() {
        return leaveReqId;
    }

    public void setLeaveReqId(long leaveReqId) {
        this.leaveReqId = leaveReqId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getLeaveStart() {
        return leaveStart;
    }

    public void setLeaveStart(LocalDate leaveStart) {
        this.leaveStart = leaveStart;
    }

    public LocalDate getLeaveEnd() {
        return leaveEnd;
    }

    public void setLeaveEnd(LocalDate leaveEnd) {
        this.leaveEnd = leaveEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
