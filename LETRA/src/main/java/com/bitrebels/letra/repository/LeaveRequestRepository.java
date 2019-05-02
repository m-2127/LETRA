package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	

}
