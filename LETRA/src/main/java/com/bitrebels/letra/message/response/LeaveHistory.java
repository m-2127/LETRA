package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.Description;
import com.bitrebels.letra.model.LeaveDates;

import java.util.HashSet;
import java.util.Set;

public class LeaveHistory {

    long leaveId;

    String leaveType;

    int duration;

    Set<Description> descriptions = new HashSet<>();

    Set<LeaveDates> leaveDates = new HashSet<>();

    public LeaveHistory(long leaveId, String leaveType, int duration) {
        this.leaveId = leaveId;
        this.leaveType = leaveType;
        this.duration = duration;
    }

    public long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(long leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Set<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public Set<LeaveDates> getLeaveDates() {
        return leaveDates;
    }

    public void setLeaveDates(Set<LeaveDates> leaveDates) {
        this.leaveDates = leaveDates;
    }
}
