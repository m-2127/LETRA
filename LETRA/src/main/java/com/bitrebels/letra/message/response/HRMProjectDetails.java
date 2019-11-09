package com.bitrebels.letra.message.response;

public class HRMProjectDetails {

    private String name;

    private String managerName;

    private int progress;

    private String status;

    public HRMProjectDetails(String projectName, String managerName, int progress, String status) {
        this.name = projectName;
        this.managerName = managerName;
        this.progress = progress;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
