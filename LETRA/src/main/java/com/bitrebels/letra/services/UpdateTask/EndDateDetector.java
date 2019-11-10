package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.model.Employee;
import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.Status;
import com.bitrebels.letra.model.Task;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class EndDateDetector {

    @Autowired
    LeaveTracker leaveTracker;

    @Autowired
    HolidayRepo holidayRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    double completedWorkHours ;
    double durationHours;
    LocalDate datePointer;

    public LocalDate deriveEndDate( long taskId, long projectId, Employee employee) {

        Task task = taskRepo.getOne(taskId);
        datePointer = task.getTaskStartDate();
        LocalDate previousEndDate = null;

        Project project = projectRepo.getOne(projectId);
        Status status = project.getStatus();

        durationHours = task.getHours()*1.0;
        completedWorkHours = 0;

        /*othertask mentioned below will always be of project type(development/maintenance)opposite to the type
        of the project of the current task*/
        Set<Task> currentTask = findOtherTask(employee, taskId);
        //the current task set above includes all tasks of the user except for the current task

        if(currentTask.isEmpty()){
            datePointer = finalCal(status);
        }else {
            /*condition below inside if condition is for the current task*/
            if (status == Status.DEVELOPMENT) {
                //4.9 hours is 70% of a particular day's hours
                status = Status.DEVELOPMENT;
                return datePointer(currentTask, previousEndDate, task, status);

            }//development status if condition

            else if (status == Status.MAINTENANCE) {
                status = Status.MAINTENANCE;
                return datePointer(currentTask, previousEndDate, task, status);
            }
        }
    return datePointer;
}

        public Set<Task> findOtherTask(Employee employee,long taskId) {

            Set<Task> tasks = employee.getTasks();
            Set<Task> currentTasks = new HashSet<>();

            for (Task othertask : tasks) {
                if (othertask.getId() != taskId) {
                    currentTasks.add(othertask);
                }
            }
            return currentTasks;
        }

        public boolean addDay(LocalDate datePointer, int duration ,/* int set,*/ Status status){
            Calendar datePointerCal = Calendar.getInstance();
            datePointerCal.clear();
            datePointerCal.set(datePointer.getYear(), datePointer.getMonthValue()-1, datePointer.getDayOfMonth());

            boolean holidaychecker = holidayRepo.existsHolidayByDate(datePointer);

            //duration indicates the maximum number of times this loop can iterate

            while((!isWorkCompleted()) && (duration>0)) {

                if (datePointerCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                        datePointerCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !holidaychecker) {


                    duration--;

                    if (/*set ==2 && */status == Status.DEVELOPMENT) {
                        completedWorkHours += 4.9;
                    } else if (/*set ==2 && */status == Status.MAINTENANCE) {
                        completedWorkHours += 2.1;
                    }

                }
                datePointerCal.add(Calendar.DAY_OF_MONTH, 1);

                holidaychecker = holidayRepo.existsHolidayByDate(LocalDateTime.ofInstant(datePointerCal.toInstant(),
                        datePointerCal.getTimeZone().toZoneId()).toLocalDate());

            }

             this.datePointer = LocalDateTime.ofInstant(datePointerCal.toInstant(),
                    datePointerCal.getTimeZone().toZoneId()).toLocalDate();

            return isWorkCompleted();
        }

        public boolean isWorkCompleted(){
            if(completedWorkHours>=durationHours){
                return true;
            }
            else {
                return false;
            }
        }

        public LocalDate datePointer(Set<Task> currentTask,LocalDate previousEndDate,Task task, Status status){

        for (Task otherTask : currentTask) {

            int days_1 ;
            //-2 to remove the days inclusive of previousenddate and othertask start date
            if(previousEndDate!=null) {
                if (((previousEndDate.isAfter(task.getTaskStartDate())) || (previousEndDate.isEqual(task.getTaskStartDate()))) &&
                        otherTask.getTaskStartDate().isAfter(task.getTaskStartDate())) {
                    days_1 = leaveTracker.countWorkingDays(previousEndDate, otherTask.getTaskStartDate()) - 2;
                    if(addDay(datePointer, days_1, /*2,*/status)){
                        System.out.println("shit 1");
                        return datePointer;
                    }
                }
            }

            if (otherTask.getTaskEndDate().isBefore(task.getTaskStartDate())) {
                continue;
            }

            if((task.getTaskStartDate().isEqual(otherTask.getTaskEndDate()))){
                if(addDay(datePointer,1,/*2,*/status)){
                    return datePointer;
                }
            }

            if(((otherTask.getTaskStartDate().isEqual(task.getTaskStartDate()))|| (otherTask.getTaskStartDate().isBefore(task.getTaskStartDate())))){

                days_1 = leaveTracker.countWorkingDays(task.getTaskStartDate(), otherTask.getTaskEndDate());
            //    days_2 = leaveTracker.countWorkingDays(task.getTaskStartDate(), otherTask.getTaskStartDate());

                if(addDay(datePointer,days_1,/*2,*/status)){
                    return datePointer;
                }
            }

            if(otherTask.getTaskStartDate().isAfter(task.getTaskStartDate())){
                days_1 = leaveTracker.countWorkingDays(otherTask.getTaskStartDate(), otherTask.getTaskEndDate());
                if(addDay(datePointer,days_1,/*2,*/status)){
                    return datePointer;
                }
            }
            previousEndDate = otherTask.getTaskEndDate();
        } //foreach loop if condition

        return finalCal(status);
    }

    public LocalDate finalCal(Status status){
        if(!isWorkCompleted()){
            double remainingDurationHours = durationHours - completedWorkHours;
            int remainingDurationDays;

            if(status == Status.DEVELOPMENT){
                remainingDurationDays = (int)Math.ceil(remainingDurationHours/4.9);
            }
            else {
                remainingDurationDays = (int) Math.ceil(remainingDurationHours / 2.1);
            }
            addDay(datePointer,remainingDurationDays,/*2,*/status);
        }
        return datePointer;
    }
    }

