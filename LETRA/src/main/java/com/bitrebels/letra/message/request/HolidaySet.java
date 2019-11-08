package com.bitrebels.letra.message.request;

import java.util.ArrayList;
import java.util.List;

public class HolidaySet {

    private List<ApplyHoliday> holidaySet
            = new ArrayList<>();

    public List<ApplyHoliday> getHolidaySet() {
        return holidaySet;
    }

    public void setHolidaySet(List<ApplyHoliday> holidaySet) {
        this.holidaySet = holidaySet;
    }
}
