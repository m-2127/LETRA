package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.HRManager;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

@Service
public class LeaveQuotaCal {

    @Autowired
    UserRepository userRepo;

    int month, quarter;

    AnnualLeave annualLeave;
    CasualLeave casualLeave;
    MaternityLeave maternityLeave;
    NoPayLeave noPayLeave;
    SickLeave sickLeave;

    public User updateQuotaOnRegistration(User user){

        month = LocalDate.now().getMonthValue();
        quarter = month-1/3 + 1;

         annualLeave = new AnnualLeave();
         casualLeave = new CasualLeave();
         maternityLeave = new MaternityLeave();
         noPayLeave = new NoPayLeave();
         sickLeave = new SickLeave();


        if(quarter == 1){
            setQuota(15,12);
        }
        else if(quarter == 2){
            setQuota(11,9);
        }
        else if(quarter == 3){
            setQuota(7,6);
            }
        else{
            setQuota(3,3);
        }

        setAdjustedQuotaToUser(user);

        return user;

        }

    public void updateQuotaAnnually(HRManager hrManager){

        Set<User> userSet = hrManager.getUserSet();

        User currentUser = null;

        Iterator<User> iterator = userSet.iterator();

        while(iterator.hasNext()) {
            currentUser = iterator.next();


            annualLeave = currentUser.getAnnualQuota();
            casualLeave = currentUser.getCasualQuota();
            maternityLeave = currentUser.getMaternityQuota();
            noPayLeave = currentUser.getNoPayQuota();
            sickLeave = currentUser.getSickQuota();

            setQuota(15, 12);

            setAdjustedQuotaToUser(currentUser);

            userRepo.save(currentUser);

        }
    }


    public void setQuota(int x, int y){

        annualLeave.setTotalLeaves(x);
        annualLeave.setRemainingLeaves(x);
        annualLeave.setLeavesTaken(0);

        casualLeave.setTotalLeaves(y);
        casualLeave.setRemainingLeaves(y);
        casualLeave.setLeavesTaken(0);

        sickLeave.setTotalLeaves(y);
        sickLeave.setRemainingLeaves(y);
        sickLeave.setLeavesTaken(0);

        noPayLeave.setLeavesTaken(0);
        maternityLeave.setLeavesTaken(0);
    }

    public void setAdjustedQuotaToUser(User user){

        user.setAnnualQuota(annualLeave);
        user.setCasualQuota(casualLeave);
        user.setMaternityQuota(maternityLeave);
        user.setNoPayQuota(noPayLeave);
        user.setSickQuota(sickLeave);
    }
    }

