package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.ReportingManager;

public interface RMRepository extends JpaRepository<ReportingManager, Long> {
	//ReportingManager findByProject(Project project);
}
