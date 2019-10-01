package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;

import java.util.List;

public class EmployeeQuotaHome {
    List<LeaveQuota> leavequota;

    public EmployeeQuotaHome(List<LeaveQuota> leavequota) {
        this.leavequota = leavequota;
    }

    public List<LeaveQuota> getLeavequota() {
        return leavequota;
    }

    public void setLeavequota(List<LeaveQuota> leavequota) {
        this.leavequota = leavequota;
    }
}
