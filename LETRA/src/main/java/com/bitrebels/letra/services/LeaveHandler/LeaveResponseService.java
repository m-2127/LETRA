package com.bitrebels.letra.services.LeaveHandler;

import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.LeaveDates;
import com.bitrebels.letra.repository.LeaveDatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveResponseService {

    @Autowired
    LeaveDatesRepo leaveDatesRepo;

    public Leave saveLeaveDates(List<String> dates , Leave leave){
        for(String date : dates) {
            LocalDate localDate = LocalDate.parse(date);
            LeaveDates temp = new LeaveDates(localDate);
            leave.getLeaveDates().add(temp);
            leaveDatesRepo.save(temp);
        }

        return leave;
    }
}
