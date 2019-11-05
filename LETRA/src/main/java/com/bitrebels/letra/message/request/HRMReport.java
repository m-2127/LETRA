package com.bitrebels.letra.message.request;

import com.bitrebels.letra.services.Date.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class HRMReport {

    @NotNull
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate startDate;

    @NotNull
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate finishDate;

    private long projectId;

    private long employeeId;

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

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
