package com.bitrebels.letra.services.Date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateToLocalDate {
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
