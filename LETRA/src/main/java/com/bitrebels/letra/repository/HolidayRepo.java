package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Holiday;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface HolidayRepo extends JpaRepository <Holiday, Long> {

    int countByDateBetween( LocalDate startDate, LocalDate currentDate);

    boolean existsHolidayByDate(LocalDate date);
}
