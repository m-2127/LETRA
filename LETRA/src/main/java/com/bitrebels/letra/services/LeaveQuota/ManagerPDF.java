package com.bitrebels.letra.services.LeaveQuota;

import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.CasualLeave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.leavequota.SickLeave;
import com.bitrebels.letra.repository.LeaveRepo;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ManagerPDF {

    @Autowired
    LeaveRepo leaveRepo;

    Employee employee = null;
    Iterator<Employee> employeeIterator;
    //to set the each employee and their corresponding leave consumption related the particular project
    Map<Employee , Integer> employeeLeavesTakenMap = new HashMap<>();
    Map<Employee , Integer> employeeRemainingAnnualLeaveMap = new HashMap<>();
    Map<Employee , Integer> employeeRemainingCasualLeaveMap = new HashMap<>();
    Map<Employee , Integer> employeeRemainingSickLeaveMap = new HashMap<>();

    public void pdfGenerator(List<Employee> employeeList, UserRepository userRepo,
                             ReportingManager rm, Project project){

        employeeIterator = employeeList.iterator();

        while(employeeIterator.hasNext()){

            employee = employeeIterator.next();

            User user = userRepo.findById(employee.getEmployeeId()).get();
            Set<LeaveQuota> leaveQuotas = user.getLeaveQuotas();

            calculateLeavesTakenByEmployee(project,rm);

            remainingLeaveCalOfEmployees(leaveQuotas);


        }
    }

    public void calculateLeavesTakenByEmployee(Project project, ReportingManager rm){

        int leavesTaken = 0;
        // returns the leaves taken by the current employee from the start date of the project till the date input from the frontend
        List<Leave> leave = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndReportingManager(
                project.getStartDate(), LocalDate.now() ,employee,rm);

        Iterator<Leave> leaveIterator = leave.iterator();

        while(leaveIterator.hasNext()){
            Leave currentLeave = leaveIterator.next();
            leavesTaken += currentLeave.getDuration();
        }

        employeeLeavesTakenMap.put( employee, leavesTaken );
    }

    public void remainingLeaveCalOfEmployees(Set<LeaveQuota> leaveQuotas){
        Iterator<LeaveQuota> leaveQuotaIterator = leaveQuotas.iterator();

        while(leaveQuotaIterator.hasNext()){
            LeaveQuota currentQuota = leaveQuotaIterator.next();
            if(currentQuota instanceof SickLeave ){
                SickLeave y = (SickLeave)currentQuota;
                int remainingLeaves = y.getRemainingLeaves();
                employeeRemainingSickLeaveMap.put(employee,remainingLeaves);
            }
            else if(currentQuota instanceof CasualLeave){
                CasualLeave y = (CasualLeave)currentQuota;
                int remainingLeaves = y.getRemainingLeaves();
                employeeRemainingCasualLeaveMap.put(employee,remainingLeaves);
            }
            else if(currentQuota instanceof AnnualLeave){
                AnnualLeave y = (AnnualLeave) currentQuota;
                int remainingLeaves = y.getRemainingLeaves();
                employeeRemainingAnnualLeaveMap.put(employee,remainingLeaves);
            }
            else{
                continue;
            }
        }
    }


}
