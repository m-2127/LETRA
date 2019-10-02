package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.Leave;

public class EmpNotificationDetails {

    Leave leave;

    String managerName;

    public EmpNotificationDetails(Leave leave , String managerName) {
        this.leave = leave;
        this.managerName = managerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }
}
