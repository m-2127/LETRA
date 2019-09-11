package com.bitrebels.letra.message.request;

import com.bitrebels.letra.model.Holiday;

import java.util.ArrayList;
import java.util.List;

public class HolidaySet {

    private List<Holiday> holidays = new ArrayList<>();

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }
}
