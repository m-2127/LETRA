package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.LeaveDates;
import com.bitrebels.letra.repository.LeaveDatesRepo;
import com.bitrebels.letra.repository.LeaveRepo;
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

    @Autowired
    LeaveRepo leaveRepo;

    public Leave saveLeaveDates(List<String> dates , Leave leave){
        for(String date : dates) {
            LocalDate localDate = LocalDate.parse(date);
            LeaveDates temp = new LeaveDates(localDate);
            leave.getLeaveDates().add(temp);
          //  leaveDatesRepo.save(temp);
        }

        return leave;
    }

    public Leave saveLeaveDatesofHRM(List<LocalDate> dates , Leave leave){
        for(LocalDate date : dates) {
            LeaveDates temp = new LeaveDates(date);
            leave.getLeaveDates().add(temp);
            leaveDatesRepo.save(temp);
        }

        return leave;
    }

    public int updateDatesWithCurrentResponse(Set<LeaveDates> initialDateList , List<String> finalDateList , Leave leave){

        int returnValue=0;

//        for(String date : finalDateList) {
//            LocalDate localDate = LocalDate.parse(date);
//
//            Iterator<LeaveDates> leaveDatesIterator = initialDateList.iterator();
//            LocalDate testDate = null;
//
//            while(leaveDatesIterator.hasNext()){
//
//                LeaveDates currentLeaveDate = leaveDatesIterator.next();
//
//                if(localDate.isEqual(currentLeaveDate.getDate())){
//                    testDate = localDate;
//                }
//
//            }
//            if(testDate==null){
//                leaveDatesIterator.remove();
//            }
//
//            leaveDatesRepo.delete(leaveDatesRepo.findByDate(localDate));
//
//        }
        for(LeaveDates leaveDates : initialDateList) {

            LocalDate testDate = null;

            Iterator<String> leaveDatesIterator = finalDateList.iterator();

            while(leaveDatesIterator.hasNext()){

                LocalDate localDate = LocalDate.parse(leaveDatesIterator.next());

                if(localDate.isEqual(leaveDates.getDate())){
                    testDate = localDate;
                }

            }
            if(testDate==null){
                leaveDatesRepo.delete(leaveDatesRepo.findByDate(leaveDates.getDate()));
                initialDateList.remove(leaveDates);
            }



        }
        leave.setLeaveDates(initialDateList);
        leaveRepo.save(leave);
        return initialDateList.size();
    }
}
