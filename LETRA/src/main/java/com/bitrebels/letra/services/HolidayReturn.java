package com.bitrebels.letra.services;

import com.bitrebels.letra.message.response.HolidayDisplay;
import com.bitrebels.letra.message.response.HolidayDisplayReturn;
import com.bitrebels.letra.model.Holiday;
import com.bitrebels.letra.repository.HolidayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class HolidayReturn {

    @Autowired
    HolidayRepo holidayRepo;

    public HolidayDisplayReturn returnHoliday() {
        List<Holiday> holidaysList = holidayRepo.findAll();
        Iterator<Holiday> holidayIterator = holidaysList.iterator();

        Set<HolidayDisplay> holidayDisplaySet = new HashSet<>();

        while (holidayIterator.hasNext()) {
            Holiday holiday = holidayIterator.next();
            HolidayDisplay holidayDisplay = new HolidayDisplay(holiday.getDescription(), holiday.getDate());
            holidayDisplaySet.add(holidayDisplay);
        }

        return new HolidayDisplayReturn(holidayDisplaySet);

    }
}
