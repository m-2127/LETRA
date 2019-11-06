package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, Long> {
//    int countByStartDateAfterAndFinishDateBefore(LocalDate startDate, LocalDate currentDate);
//    List<Leave> findByEmployeeAndDatesBetween(Employee employee, LocalDate startDate, LocalDate endDate);
//    int countLeavesByDatesBetween(LeaveDates startDate, LeaveDates endDate);
//    List<Leave> findByLeaveDates_Date(LocalDate localDate);
    Set<Leave> findByLeaveDates_DateBetweenAndEmployeeAndReportingManagerAndApproval(
            LocalDate startDate, LocalDate endDate, Employee employee , ReportingManager rm,boolean b);

    Set<Leave> findByLeaveDates_DateBetweenAndApproval(LocalDate startDate, LocalDate endDate,boolean b);

    Set<Leave> findByLeaveDates_DateBetweenAndReportingManagerAndApproval(
            LocalDate startDate, LocalDate endDate, ReportingManager rm,boolean b);

    Set<Leave> findByLeaveDates_DateBetweenAndEmployeeAndApproval(
            LocalDate startDate, LocalDate endDate, Employee employee,boolean b);

    Leave findLeaveByLeaveRequest(LeaveRequest leaveRequest);

    Set<Leave> findLeavesByEmployee(Employee employee);

    List<Leave> findByReportingManagerAndStatus(ReportingManager reportingManager, LeaveStatus status);
}
