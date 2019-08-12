package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.User;
import java.util.List;

public interface LeaveQuotaRepository extends JpaRepository<LeaveQuota, Long> {
	List<LeaveQuota> findByUser(User user);
//	List<Leave> findByLeaveQuota(String leaveType);
}
