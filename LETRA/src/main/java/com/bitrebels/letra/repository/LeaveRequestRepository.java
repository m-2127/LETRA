package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

 //   List<LeaveRequest> findByReportingManagers(ReportingManager reportingManagerm);
}
