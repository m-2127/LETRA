package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.leavequota.LeaveQuota;

public interface LeaveQuotaRepository extends JpaRepository<LeaveQuota, Long> {

}
