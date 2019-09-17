package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

@Service
public class UpdateQuota {
    @Autowired
    UserRepository userRepo;
    public  void updateQuota(String leaveType, int noOfDays, User user){

        Set<LeaveQuota> leaveQuotas = user.getLeaveQuotas();

        if(leaveType.equalsIgnoreCase("annual")){
            Iterator<LeaveQuota> leaveQuotaIterator = leaveQuotas.iterator();
            while(leaveQuotaIterator.hasNext()){
                LeaveQuota currentQuota = leaveQuotaIterator.next();
                if(currentQuota instanceof AnnualLeave){
                    AnnualLeave y = (AnnualLeave)currentQuota;
                    y.addTaken(noOfDays);
                    y.reduceRemaining(noOfDays);
                    break;
                }
            }

        }
        else if(leaveType.equalsIgnoreCase("casual")){
            Iterator<LeaveQuota> leaveQuotaIterator = user.getLeaveQuotas().iterator();
            while(leaveQuotaIterator.hasNext()){
                LeaveQuota currentQuota = leaveQuotaIterator.next();
                if(currentQuota instanceof CasualLeave){
                    CasualLeave y = (CasualLeave)currentQuota;
                    y.addTaken(noOfDays);
                    y.reduceRemaining(noOfDays);
                    break;
                }
            }

        }
        else if(leaveType.equalsIgnoreCase("sick")){
            Iterator<LeaveQuota> leaveQuotaIterator = user.getLeaveQuotas().iterator();
            while(leaveQuotaIterator.hasNext()){
                LeaveQuota currentQuota = leaveQuotaIterator.next();
                if(currentQuota instanceof SickLeave){
                    SickLeave y = (SickLeave)currentQuota;
                    y.addTaken(noOfDays);
                    y.reduceRemaining(noOfDays);
                    break;
                }
            }
        }
        else{
            Iterator<LeaveQuota> leaveQuotaIterator = user.getLeaveQuotas().iterator();
            while(leaveQuotaIterator.hasNext()){
                LeaveQuota currentQuota = leaveQuotaIterator.next();
                if(currentQuota instanceof NoPayLeave){
                    NoPayLeave y = (NoPayLeave)currentQuota;
                    y.addTaken(noOfDays);
                    break;
                }
            }

        }
        user.setLeaveQuotas(leaveQuotas);
        userRepo.save(user);
    }
}
