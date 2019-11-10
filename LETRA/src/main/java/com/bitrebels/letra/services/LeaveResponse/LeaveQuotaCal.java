package com.bitrebels.letra.services.LeaveResponse;

import com.bitrebels.letra.model.HRManager;
import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.UserRepository;
import com.bitrebels.letra.repository.leavequotarepo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class LeaveQuotaCal {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AnnualRepo annualRepo;

    @Autowired
    CasualRepo casualRepo;

    @Autowired
    MaternityRepo maternityRepo;

    @Autowired
    SickRepo sickRepo;

    @Autowired
    NoPayRepo noPayRepo;

    @Autowired
    LeaveQuotaRepository leaveQuotaRepository;

    int month, quarter;

    Set<LeaveQuota> leaveQuotas = null;
    AnnualLeave annualLeave;
    CasualLeave casualLeave;
    MaternityLeave maternityLeave;
    NoPayLeave noPayLeave;
    SickLeave sickLeave;

    public User updateQuotaOnRegistration(User user){

        month = LocalDate.now().getMonthValue();
        System.out.println(month);
        quarter = ((month-1)/3 )+ 1;
        System.out.println(quarter);



         annualLeave = new AnnualLeave();
         casualLeave = new CasualLeave();
         maternityLeave = new MaternityLeave();
         noPayLeave = new NoPayLeave();
         sickLeave = new SickLeave();
         leaveQuotas = new HashSet<>();

         annualLeave.setUser(user);
         casualLeave.setUser(user);
         noPayLeave.setUser(user);
         sickLeave.setUser(user);
         maternityLeave.setUser(user);

         leaveQuotas.add(annualLeave);
         leaveQuotas.add(sickLeave);
         leaveQuotas.add(noPayLeave);
         leaveQuotas.add(maternityLeave);
         leaveQuotas.add(casualLeave);

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

        user.setLeaveQuotas(leaveQuotas);

        annualRepo.save(annualLeave);
        casualRepo.save(casualLeave);
        maternityRepo.save(maternityLeave);
        sickRepo.save(sickLeave);
        noPayRepo.save(noPayLeave);

        return user;

        }

    public void updateQuotaAnnually(HRManager hrManager){

        Set<User> userSet = hrManager.getUserSet();

        User currentUser = null;

        Iterator<User> iterator = userSet.iterator();

        while(iterator.hasNext()) {
            currentUser = iterator.next();

            Set<LeaveQuota> currentUserLeaveQuotas = currentUser.getLeaveQuotas();


            Iterator<LeaveQuota> leaveQuotaIterator1 = currentUserLeaveQuotas.iterator();

            while(leaveQuotaIterator1.hasNext()) {

                LeaveQuota leaveQuota = leaveQuotaIterator1.next();

                if (leaveQuota instanceof AnnualLeave) {
                    annualLeave = (AnnualLeave) leaveQuota;

                }
            }

            Iterator<LeaveQuota> leaveQuotaIterator2 = currentUserLeaveQuotas.iterator();
            while(leaveQuotaIterator2.hasNext()) {

                LeaveQuota leaveQuota = leaveQuotaIterator2.next();

                if(leaveQuota instanceof CasualLeave){
                    casualLeave = (CasualLeave) leaveQuota;

                }
            }
            Iterator<LeaveQuota> leaveQuotaIterator3 = currentUserLeaveQuotas.iterator();
            while(leaveQuotaIterator3.hasNext()) {

                LeaveQuota leaveQuota = leaveQuotaIterator3.next();

                if(leaveQuota instanceof MaternityLeave){
                    maternityLeave = (MaternityLeave) leaveQuota;
                }
            }

            Iterator<LeaveQuota> leaveQuotaIterator4 = currentUserLeaveQuotas.iterator();
            while(leaveQuotaIterator4.hasNext()) {

                LeaveQuota leaveQuota = leaveQuotaIterator4.next();

                if(leaveQuota instanceof NoPayLeave){
                    noPayLeave = (NoPayLeave) leaveQuota;

                }
            }

            Iterator<LeaveQuota> leaveQuotaIterator5 = currentUserLeaveQuotas.iterator();
            while(leaveQuotaIterator5.hasNext()) {

                LeaveQuota leaveQuota = leaveQuotaIterator5.next();

                if (leaveQuota instanceof SickLeave) {
                    sickLeave = (SickLeave) leaveQuota;

                }
            }

            setQuotaAnnually(15, 12,currentUser);

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


    public void setQuotaAnnually(int x, int y,User user){

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

        annualRepo.save(annualLeave);
        casualRepo.save(casualLeave);
        maternityRepo.save(maternityLeave);
        sickRepo.save(sickLeave);
        noPayRepo.save(noPayLeave);

        userRepo.save(user);

    }
    }

