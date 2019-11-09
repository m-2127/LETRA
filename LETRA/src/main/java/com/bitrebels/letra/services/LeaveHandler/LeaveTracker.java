
package com.bitrebels.letra.services.LeaveHandler;

import com.bitrebels.letra.model.Status;
import com.bitrebels.letra.repository.HolidayRepo;
import com.bitrebels.letra.repository.LeaveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;


@Service
public class LeaveTracker {

    @Autowired
    HolidayRepo holidayRepo;

    @Autowired
    LeaveRepo leaveRepo;

//    the workDays in below functions are multiplied by 7 considering the
//    number of work hours per day as 7

    public double requiredWork(LocalDate firstDate, LocalDate secondDate, LocalDate taskEndDate, Status status,
                               int totalHoursOfTask){
        //firstDate= task start date
        //secondDate= leave start date

        int workingDays;

        //though start date is after the current date, the countworkingdays method will interchange those two and do the calculation
        if(taskEndDate.isBefore(secondDate)){
            //this is if the task ends before the leave start date
            workingDays = countWorkingDays(firstDate,taskEndDate);
        }
        else if(firstDate.isAfter(secondDate)){
            return 0.0;
        }
        else{
            workingDays = countWorkingDays(firstDate,secondDate.minusDays(1l));
        }

        double requiredWork = countHours(workingDays, status);

        if( requiredWork >= totalHoursOfTask ) {
            return totalHoursOfTask;
        }else{
            return requiredWork;
        }

    }

    public double leftWorkHours( LocalDate taskEndDate,
                                LocalDate leaveEndDate, Status status){
        //firstDate= leave end date

        int workingDays=0;

        //though start date is after the current date, the countworkingdays method will interchange those two and do the calculation
        if(taskEndDate.isBefore(leaveEndDate)||taskEndDate.isEqual(leaveEndDate)){
            //this is if the task ends before the leave start date
            workingDays = 0;
        }
//        else if(taskEndDate.isAfter(leaveEndDate))
        else{
            workingDays = countWorkingDays(leaveEndDate.plusDays(1l),taskEndDate);
        }

        return countHours(workingDays, status);
    }

    public int countWorkingDays(LocalDate initialDate ,LocalDate finalDate ){

        int workingDays=0;

        Calendar initialCal = Calendar.getInstance();
        initialCal.clear();
        initialCal.set(initialDate.getYear(), initialDate.getMonthValue()-1, initialDate.getDayOfMonth());

        Calendar finalCal = Calendar.getInstance();
        finalCal.clear();
        finalCal.set(finalDate.getYear(), finalDate.getMonthValue()-1, finalDate.getDayOfMonth());

        if (initialCal.getTimeInMillis() == finalCal.getTimeInMillis()) {
            return 1;
        }

        if (initialCal.getTimeInMillis() > finalCal.getTimeInMillis()) {
            initialCal.set(finalDate.getYear(), finalDate.getMonthValue()-1, finalDate.getDayOfMonth());
            finalCal.set(initialDate.getYear(), initialDate.getMonthValue()-1, initialDate.getDayOfMonth());

            LocalDate temp = initialDate;
            initialDate = finalDate;
            finalDate = temp;
        }

        do {
            //including start date

            if (initialCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && initialCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workingDays;
            }
            initialCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (initialCal.getTimeInMillis() <= finalCal.getTimeInMillis()); //including end date as i have added = sign in this line as <=

        int actualWorkingDays = actualWorkingDays(workingDays , initialDate , finalDate);

        return actualWorkingDays;
    }

    public double availableHoursForLeave(double actualProgressHours, double requiredProgressHours,
                                         double remainingWork, double availableTime , int workingDays,
                                         Status status, LocalDate leaveEndDate, LocalDate taskEndDate) {
        //workdays parameter in this method is the number of workdays within applied leave range


        boolean preCondition = false;
        double noOfleaveDays = countHours(workingDays , status);
        double workDaysInHours = countHours(workingDays , status);

        double availableLeaveHours = 0.0;

        if(actualProgressHours>=requiredProgressHours){
            preCondition = true;
        }
        if(preCondition) {


            /*the first condition in while loop below will prevent counting the days beyond the task end date
            which means at the end of the task actual progress will be equal to required and hence the loop
            will not run*/
                while (actualProgressHours > requiredProgressHours && workDaysInHours > 0) {

                    double diff = actualProgressHours - requiredProgressHours;
                    if(status.equals(Status.DEVELOPMENT)&& diff>=4.9){
                        requiredProgressHours += 4.9;
                        availableLeaveHours +=4.9;
                        workDaysInHours -= 4.9;

                    }else if(status.equals(Status.MAINTENANCE) && diff>=2.1){
                        requiredProgressHours += 2.1;
                        availableLeaveHours +=2.1;
                        workDaysInHours -= 2.1;
                    }else{
                        requiredProgressHours += diff;
                        availableLeaveHours += diff;
                        workDaysInHours -= diff;

                    }
                    if(workDaysInHours<=0.0){
                        return availableLeaveHours;
                    }
                }

                if(availableTime>=remainingWork){//maximum hours is returned if he has time to complete the work after returning from leave
                    return noOfleaveDays;
                }else{//check OT
                    if(status.equals(Status.DEVELOPMENT)&& taskEndDate.isAfter(leaveEndDate)){
                        availableTime = recalculateOfAvailableTime(leaveEndDate,taskEndDate);
                        if(availableTime>=remainingWork){

                            return noOfleaveDays;
                        }
                     }
                        return availableLeaveHours;
                }


        }else{
            if(status.equals(Status.DEVELOPMENT)&& taskEndDate.isAfter(leaveEndDate)){
                availableTime = recalculateOfAvailableTime(leaveEndDate,taskEndDate);
                if(availableTime>=remainingWork){
                    return workDaysInHours;
                }
            }
            return availableLeaveHours;
        }
    }

    //returns the actual working days meaning, working days excluding holidays
    public int actualWorkingDays(int workingDays, LocalDate initialDate, LocalDate finalDate){
        int actualWorkingDays = workingDays - holidayRepo.countByDateBetween(initialDate, finalDate);
        return actualWorkingDays;
    }

    public double remainingWorkInHours(double currentProgress, int totalHours){
        return totalHours-currentProgress;
    }


    public double currentProgress(LocalDate leaveStartDate , int actualCurrentProgress, Timestamp lastUpdate,
                               Status status, int totalHoursOfTheTask,LocalDate taskStart){
        /* This method is used to calculate the number of hours that have been worked till the start date of
        leave but not updated to the database. That is the work done + the work which will be done till the
         start date of the leave. Returns the total number of hours completed as at the start date of the leave
         */

        LocalDate dateOfLeave = leaveStartDate;


        //converts timestamp to LocalDate
        LocalDate lastUpdateDate = lastUpdate.toLocalDateTime().toLocalDate();

        int daysSinceLastUpdate = countWorkingDays(lastUpdateDate, dateOfLeave);

        if(status.equals(Status.DEVELOPMENT)) {
            double currentProgress;
            if(lastUpdateDate.isBefore(taskStart)){
                currentProgress = countWorkingDays(taskStart,leaveStartDate)*4.9 + actualCurrentProgress;
            }else{
                currentProgress = daysSinceLastUpdate *4.9 + actualCurrentProgress;
            }


            if( currentProgress >= totalHoursOfTheTask ) {
                return totalHoursOfTheTask;
            }else{
                return currentProgress;
            }
        }
        else{

            double currentProgress;
            if(lastUpdateDate.isBefore(taskStart)){
                currentProgress = countWorkingDays(taskStart,leaveStartDate)*2.1 + actualCurrentProgress;
            }else{
                currentProgress = daysSinceLastUpdate *2.1 + actualCurrentProgress;
            }

            if( currentProgress >= totalHoursOfTheTask ) {
                return totalHoursOfTheTask;
            }else{
                return currentProgress;
            }
        }
    }

    /*This method is used to check whether work can be completed within the remainingDays
    * (remainingDays = leaveEndDate-taskEndDate)*/
    public double recalculateOfAvailableTime(LocalDate leaveEndDate, LocalDate taskEndDate){
            return countWorkingDays(leaveEndDate.plusDays(1l),taskEndDate)*6.0;

    }

    public int canWorkBeCompletedWithOT(int dif,int workingDaysAvailable){
        int OTAvailable = workingDaysAvailable*2;
        int leftWorkInHours = dif*7;
        if (OTAvailable >= leftWorkInHours) {
        return 0;
        }
        else{
            return (leftWorkInHours-OTAvailable);
        }
        }

        double countHours(int workingDays, Status status){
            if(status == Status.DEVELOPMENT){
                return workingDays*4.9;
            }else if(status == Status.MAINTENANCE){
                return workingDays*2.1;
            }
            else{
                return workingDays*0;
            }
        }

}

