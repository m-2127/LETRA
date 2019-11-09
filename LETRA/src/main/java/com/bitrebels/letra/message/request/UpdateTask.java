package com.bitrebels.letra.message.request;

import com.bitrebels.letra.services.Date.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UpdateTask {

    @NotNull
    private Long taskId;

    @NotNull
    private Long employeeId;

    private int progress;

    private String status;

    @NotNull
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate startdate;

//    @NotNull
//    @JsonDeserialize(using = DateHandler.class)
//    private LocalDate enddate;

    private int duration;

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

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

//    public LocalDate getEnddate() {
//        return enddate;
//    }
//
//    public void setEnddate(LocalDate enddate) {
//        this.enddate = enddate;
//    }
}
