package com.bitrebels.letra.message.response;

import java.time.LocalDate;

public class ReturnProjectDetails {

    String name;

    LocalDate startDate;

    LocalDate finishDate;

    String status;

    public ReturnProjectDetails(String name, LocalDate startDate, LocalDate finishDate, String status) {
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
