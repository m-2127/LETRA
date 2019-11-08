package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.ReportingManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

//    List<LeaveRequest> findByReportingManagers(ReportingManager reportingManagerm);
    Set<LeaveRequest> findByLeaveTypeAndFinishDateAfter(String leaveType, LocalDate date);
}
