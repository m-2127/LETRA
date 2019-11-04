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

    private String projectString;

    private String employeeString;

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

    public String getProjectString() {
        return projectString;
    }

    public void setProjectString(String projectString) {
        this.projectString = projectString;
    }

    public String getEmployeeString() {
        return employeeString;
    }

    public void setEmployeeString(String employeeString) {
        this.employeeString = employeeString;
    }
}
