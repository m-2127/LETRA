package com.bitrebels.letra.services.LeaveHandler;


import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.CasualLeave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.leavequota.NoPayLeave;
import com.bitrebels.letra.repository.ProgressRepo;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

@Service
public class ACNTypeLeaves {

    @Autowired
    LeaveTracker leaveTracker;

    @Autowired
    ProgressRepo progressRepo;

    @Autowired
    UserRepository userRepo;

    protected LocalDate leaveStartDate;
    protected LocalDate leaveEndDate;
    protected LocalDate taskStartDate;
    protected LocalDate taskEndDate;
    int availableDaysForLeave = 0 ;

    public Progress  calculateRecommendation(Task task, int workingDays, LeaveRequest leaveRequest) {

        double currentProgressHours;

        taskStartDate = task.getTaskStartDate();
        taskEndDate = task.getTaskEndDate();
        leaveStartDate = leaveRequest.getSetDate();
        leaveEndDate = leaveRequest.getFinishDate();

        if(task.getStatus().equals(Status.COMPLETED)|| task.getTaskEndDate().isBefore(leaveRequest.getSetDate())||
                task.getTaskStartDate().isAfter(leaveRequest.getFinishDate())||
                task.getTaskStartDate().isEqual(leaveRequest.getFinishDate())){
            return null;
        }

        //requiredWork() method can be used either to calculate required work or remaining work


        //required progress refers to the expected progress as of the date of the leave start
        //tested
        double requiredProgressHours = leaveTracker.requiredWork(taskStartDate,leaveStartDate,
                                taskEndDate,task.getStatus(),task.getHours());

         /*current progress refers to the actual progress as at the leave start date(An assumption is made here
         considering that the employee will complete 4.9/2.1 hours of the task each from the date of leave being
         applied to the date of leave start)*/
         if(taskStartDate.isBefore(leaveStartDate)) {
             //tested
             currentProgressHours = leaveTracker.currentProgress(leaveStartDate, task.getProgress(),
                     task.getUpdateTime(), task.getStatus(),task.getHours(),taskStartDate);
         }else{
             currentProgressHours = 0.0;
         }

        //number of hours remaining to complete the task from the leave end date
        //tested
        double hoursOfWorkAvailable = leaveTracker.leftWorkHours(taskEndDate,
                leaveEndDate,task.getStatus());

        //actual work left from the day of the leave
        //tested
        double remainingWorkInHours = leaveTracker.remainingWorkInHours( currentProgressHours, task.getHours());

        double availableHoursForLeave = leaveTracker.availableHoursForLeave(currentProgressHours,
                requiredProgressHours,remainingWorkInHours,hoursOfWorkAvailable, workingDays, task.getStatus(),
                leaveEndDate,taskEndDate);

        if(task.getStatus().equals(Status.DEVELOPMENT)){
            availableDaysForLeave = (int)Math.ceil(availableHoursForLeave/4.9);
        }else{
            availableDaysForLeave = (int)Math.ceil(availableHoursForLeave/2.1);
        }


        currentProgressHours = round(currentProgressHours , 3);
        requiredProgressHours = round(requiredProgressHours , 3);
        remainingWorkInHours = round(remainingWorkInHours , 3);
        hoursOfWorkAvailable = round(hoursOfWorkAvailable , 3);

        User user = userRepo.findById(leaveRequest.getEmployee().getEmployeeId()).get();

        Set<LeaveQuota> leaveQuotas = user.getLeaveQuotas();

        int remainingNoOfLeavesInTheQuota = leaveTracker.remainingLeavesInQuota(leaveQuotas,leaveRequest);

        if(remainingNoOfLeavesInTheQuota<availableDaysForLeave){
            availableDaysForLeave = remainingNoOfLeavesInTheQuota;
        }

        Progress progress = new Progress(currentProgressHours, requiredProgressHours, remainingWorkInHours ,
                hoursOfWorkAvailable,availableDaysForLeave , task.getTaskName());

       progressRepo.save(progress);

        return progress;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
