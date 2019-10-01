package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Progress;
import com.bitrebels.letra.model.ReportingManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepo extends JpaRepository<Progress , Long> {

    List<Progress> findProgressByManager(ReportingManager reportingManager);
}
