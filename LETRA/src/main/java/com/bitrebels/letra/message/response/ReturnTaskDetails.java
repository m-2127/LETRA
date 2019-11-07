package com.bitrebels.letra.message.response;

import java.time.LocalDate;

public class ReturnTaskDetails {

    long taskId;

    String name;

    LocalDate startDate;

    LocalDate finishDate;

    int duration;

    int progress;

    String status;

    String employee;

    public ReturnTaskDetails(long taskId, String name, LocalDate startDate, LocalDate finishDate, int duration,
                             int progress, String status, String employee) {
        this.taskId = taskId;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.duration = duration;
        this.progress = progress;
        this.status = status;
        this.employee = employee;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
}
