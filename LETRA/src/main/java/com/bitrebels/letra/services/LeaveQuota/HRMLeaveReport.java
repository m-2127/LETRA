package com.bitrebels.letra.services.LeaveQuota;

import com.bitrebels.letra.message.request.HRMReport;
import com.bitrebels.letra.message.response.HRMReportDetails;
import com.bitrebels.letra.model.Employee;
import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.ReportingManager;
import com.bitrebels.letra.repository.EmployeeRepository;
import com.bitrebels.letra.repository.LeaveRepo;
import com.bitrebels.letra.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

@Service
public class HRMLeaveReport {

    @Autowired
    LeaveRepo leaveRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    EmployeeRepository employeeRepo;

    public Set<Leave> selectLeaves(long employeeString, long projectString , LocalDate startDate,
                                   LocalDate endDate, HRMReport hrmReport){

        Set<Leave> leaveSet;

            if(employeeString==0 && projectString==0){
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndApproval(startDate, endDate,true);
            }
            else if(employeeString==0){
                long projectId = hrmReport.getProjectId();
                Project project = projectRepo.findById(projectId).get();
                ReportingManager rm = project.getRm();
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndReportingManagerAndApproval(startDate,
                        endDate,rm,true);
            }
            else if(projectString==0){

                long employeeId = hrmReport.getEmployeeId();
                Employee employee  = employeeRepo.findById(employeeId).get();

                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndApproval(startDate,
                        endDate,employee,true);
            }
            else{
                long projectId = hrmReport.getProjectId();
                Project project = projectRepo.findById(projectId).get();
                ReportingManager rm = project.getRm();

                long employeeId = hrmReport.getEmployeeId();
                Employee employee  = employeeRepo.findById(employeeId).get();

                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndReportingManagerAndApproval(
                        startDate,endDate,employee,rm,true);
            }

        return leaveSet;
    }

    public HRMReportDetails addLeave(Set<Leave> leaveSet){

        int annual=4,casual=3,sick=5,nopay=6;

        Iterator<Leave> leaveIterator = leaveSet.iterator();

        while(leaveIterator.hasNext()) {
            Leave currentLeave = leaveIterator.next();
            String leaveType = currentLeave.getLeaveType();

                if(leaveType.equalsIgnoreCase("annual")){
                    annual += currentLeave.getDuration();
                }
                else if(leaveType.equalsIgnoreCase("casual")){
                    casual += currentLeave.getDuration();
                }
                else if(leaveType.equalsIgnoreCase("sick")){
                    sick += currentLeave.getDuration();
                }
                else if(leaveType.equalsIgnoreCase("nopay")){
                    nopay += currentLeave.getDuration();
                }
                else{
                    continue;
                }
        }

        return new HRMReportDetails(annual,casual, nopay,sick);
    }
}
