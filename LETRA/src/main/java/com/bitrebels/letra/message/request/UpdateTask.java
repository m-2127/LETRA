package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdateTask {

    @NotNull
    private Long projectId;

    @NotNull
    private Long taskId;

    @NotNull
    private Long employeeId;

    private int progress;

    private String status;

    Map<Long, Long> updatedTask = new HashMap<>();

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Map<Long, Long> getUpdatedTask() {
        return updatedTask;
    }

    public void setUpdatedTask(Map<Long, Long> updatedTask) {
        this.updatedTask = updatedTask;
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
