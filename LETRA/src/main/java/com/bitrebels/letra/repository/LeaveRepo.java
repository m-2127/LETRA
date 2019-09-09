package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Leave;
import com.bitrebels.letra.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, LeaveRequest> {
//    int countByStartDateAfterAndFinishDateBefore(LocalDate startDate, LocalDate currentDate);
}
