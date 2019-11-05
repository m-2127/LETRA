package com.bitrebels.letra.services.LeaveQuota;

import com.bitrebels.letra.message.response.HRMReportDetails;
import com.bitrebels.letra.model.Employee;
import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.ReportingManager;
import com.bitrebels.letra.repository.LeaveRepo;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
public class HRMLeaveReport {

    @Autowired
    LeaveRepo leaveRepo;

    public Set<Leave> selectLeaves(String employeeString, String projectString , LocalDate startDate,
                                    LocalDate endDate, Employee employee , ReportingManager rm){

        Set<Leave> leaveSet;

            if(employeeString.equalsIgnoreCase("null") && projectString.equalsIgnoreCase("null")){
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndApproval(startDate, endDate,true);
            }
            else if(employeeString.equalsIgnoreCase("null")){
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndReportingManagerAndApproval(startDate,
                        endDate,rm,true);
            }
            else if(projectString.equalsIgnoreCase("null")){
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndApproval(startDate,
                        endDate,employee,true);
            }
            else{
                leaveSet = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndReportingManagerAnAndApproval(
                        startDate,endDate,employee,rm,true);
            }

        return leaveSet;
    }

    public HRMReportDetails addLeave(Set<Leave> leaveSet){

        int annual=0,casual=0,sick=0,nopay=0;

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
