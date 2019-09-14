package com.bitrebels.letra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.ReportingManager;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository< Project, Long> {
	Optional<Project> findById(Long id);
	
	@Query("FROM Project p where p.rm.rmId = :rmId")
	Optional<Project> findBySomething(@Param("rmId") Long rmId);
	
	Optional<Project> findByRm(ReportingManager rm);
}
