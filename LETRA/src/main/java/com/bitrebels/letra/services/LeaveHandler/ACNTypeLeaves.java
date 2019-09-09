package com.bitrebels.letra.services.LeaveHandler;


import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.Progress;
import com.bitrebels.letra.model.Status;
import com.bitrebels.letra.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ACNTypeLeaves {

    @Autowired
    LeaveTracker leaveTracker;

    protected LocalDate leaveStartDate;
    protected LocalDate leaveEndDate;
    protected LocalDate taskStartDate;
    protected LocalDate taskEndDate;
    int availableDaysForLeave = 0 ;

    public Progress  calculateRecommendation(Task task, int workingDays, LeaveRequest leaveRequest) {

        double currentProgressHours;

        taskStartDate = task.getStartDate();
        taskEndDate = task.getEndDate();
        leaveStartDate = leaveRequest.getSetDate();
        leaveEndDate = leaveRequest.getFinishDate();

        if(task.getStatus().equals(Status.COMPLETED)|| task.getEndDate().isBefore(leaveRequest.getSetDate())||
                task.getStartDate().isAfter(leaveRequest.getFinishDate())||
                task.getStartDate().isEqual(leaveRequest.getFinishDate())){
            return null;
        }

        //requiredWork() method can be used either to calculate required work or remaining work


        //required progress refers to the expected progress as of the date of the leave start
        double requiredProgressHours = leaveTracker.requiredWork(taskStartDate,leaveStartDate,
                                taskEndDate,task.getStatus());

         /*current progress refers to the actual progress as at the leave start date(An assumption is made here
         considering that the employee will complete 4.9/2.1 hours of the task each from the date of leave being
         applied to the date of leave start)*/
         if(taskStartDate.isBefore(leaveStartDate)) {
             currentProgressHours = leaveTracker.currentProgress(leaveStartDate, task.getProgress(),
                     task.getUpdateTime(), task.getStatus());
         }else{
             currentProgressHours = 0.0;
         }

        //number of hours remaining to complete the task from the leave end date
        double hoursOfWorkAvailable = leaveTracker.leftWorkHours(taskEndDate,
                leaveEndDate,task.getStatus());

        //actual work left from the day of the leave
        double remainingWorkInHours = leaveTracker.remainingWorkInHours( currentProgressHours, task.getHours());

        double availableHoursForLeave = leaveTracker.availableHoursForLeave(currentProgressHours,
                requiredProgressHours,remainingWorkInHours,hoursOfWorkAvailable, workingDays, task.getStatus(),
                leaveEndDate,taskEndDate);

        if(task.getStatus().equals(Status.DEVELOPMENT)){
            availableDaysForLeave = (int)Math.ceil(availableHoursForLeave/4.9);
        }else{
            availableDaysForLeave = (int)Math.ceil(availableHoursForLeave/2.1);
        }

        Progress progress = new Progress(currentProgressHours, requiredProgressHours, remainingWorkInHours ,
                hoursOfWorkAvailable,availableDaysForLeave);

        return progress;

    }
}
