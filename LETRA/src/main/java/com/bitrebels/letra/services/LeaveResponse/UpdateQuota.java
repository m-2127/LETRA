package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.CasualLeave;
import com.bitrebels.letra.model.leavequota.NoPayLeave;
import com.bitrebels.letra.model.leavequota.SickLeave;
import org.springframework.stereotype.Service;

@Service
public class UpdateQuota {
    public  void updateQuota(String leaveType, int noOfDays, User user){

        if(leaveType.equalsIgnoreCase("annual")){
            AnnualLeave x = user.getAnnualQuota();
            x.addTaken(noOfDays);
            x.reduceRemaining(noOfDays);
        }
        else if(leaveType.equalsIgnoreCase("casual")){
            CasualLeave x = user.getCasualQuota();
            x.addTaken(noOfDays);
            x.reduceRemaining(noOfDays);
        }
        else if(leaveType.equalsIgnoreCase("sick")){
            SickLeave x = user.getSickQuota();
            x.addTaken(noOfDays);
            x.reduceRemaining(noOfDays);
        }
        else{
            NoPayLeave x = user.getNoPayQuota();
            x.addTaken(noOfDays);
        }

    }
}
