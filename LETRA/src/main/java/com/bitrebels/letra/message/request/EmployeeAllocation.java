package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotNull;

public class EmployeeAllocation {
    @NotNull
    private Long projectId;
    @NotNull
    private Long taskId;
    @NotNull
    private Long employeeId;
    @NotNull
    private Long rmId;

    public EmployeeAllocation() {
    }

    public Long getRmId() {
        return this.rmId;
    }

    public void setRmId(Long rmId) {
        this.rmId = rmId;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
