package com.bitrebels.letra.repository.leavequotarepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveQuotaRepository extends JpaRepository<LeaveQuota, Long> {
	List<LeaveQuota> findByUser(User user);
//	List<Leave> findByLeaveQuota(String leaveType);
}
