package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.LeaveDates;
import com.bitrebels.letra.repository.LeaveDatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    public void updateDatesWithCurrentResponse(Set<LeaveDates> initialDateList , List<String> finalDateList , Leave leave){

        for(String date : finalDateList) {
            LocalDate localDate = LocalDate.parse(date);

            Iterator<LeaveDates> leaveDatesIterator = initialDateList.iterator();
            
            while(leaveDatesIterator.hasNext()){
                LeaveDates currentLeaveDate = leaveDatesIterator.next();

                if(!localDate.isEqual(currentLeaveDate.getDate())){
                    leaveDatesIterator.remove();
                }

            }

//            leaveDatesIterator.
//            for (LeaveDates leaveDate : initialDateList) {
//
//                if(!localDate.isEqual(leaveDate.getDate())){
//                       initialDateList.remove(leaveDate);
//                }
//
//            }
        }


        leave.setLeaveDates(initialDateList);
    }
}
