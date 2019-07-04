package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface HolidayRepo extends JpaRepository <Holiday, Long> {

    long countByDateBetween( LocalDate startDate, LocalDate currentDate);
}
