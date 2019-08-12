package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, LeaveRequest> {
    int countByStartDateAfterAndFinishDateBefore(LocalDate startDate, LocalDate currentDate);
}
