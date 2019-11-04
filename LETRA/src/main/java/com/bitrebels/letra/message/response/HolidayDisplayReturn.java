package com.bitrebels.letra.message.response;

import java.util.Set;

public class HolidayDisplayReturn {

    private Set<HolidayDisplay> holidays;

    public HolidayDisplayReturn(Set<HolidayDisplay> holidays) {
        this.holidays = holidays;
    }

    public Set<HolidayDisplay> getHolidays() {
        return holidays;
    }

    public void setHolidays(Set<HolidayDisplay> holidays) {
        this.holidays = holidays;
    }
}
