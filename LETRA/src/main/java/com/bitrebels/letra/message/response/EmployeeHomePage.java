package com.bitrebels.letra.message.response;

import java.time.LocalDate;

public class EmployeeHomePage {

    LocalDate fromDate;

    LocalDate toDate;

    int duration;

    String leaveType;

    String status;

    public EmployeeHomePage(LocalDate fromDate, LocalDate toDate, int duration, String leaveType, String status) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.duration = duration;
        this.leaveType = leaveType;
        this.status = status;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
