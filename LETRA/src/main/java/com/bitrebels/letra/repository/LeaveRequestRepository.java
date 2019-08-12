package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.ReportingManager;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.LeaveRequest;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByReportingManagers(ReportingManager reportingManagerm);
}
