package com.bitrebels.letra.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Holiday {

    @Id
    private long holidayId;

    private LocalDate date;

    public long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(long holidayId) {
        this.holidayId = holidayId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
