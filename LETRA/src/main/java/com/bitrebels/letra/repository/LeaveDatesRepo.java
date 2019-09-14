package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.LeaveDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveDatesRepo extends JpaRepository<LeaveDates, Long> {
}
