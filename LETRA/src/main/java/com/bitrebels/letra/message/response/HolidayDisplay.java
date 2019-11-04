package com.bitrebels.letra.message.response;

import java.time.LocalDate;

public class HolidayDisplay {

    private String title;

    private LocalDate start;

    public HolidayDisplay(String title, LocalDate start) {
        this.title = title;
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }
}
