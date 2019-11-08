package com.bitrebels.letra.message.request;

import com.bitrebels.letra.services.Date.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public class ApplyHoliday {
    private String title;

    private String start;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
