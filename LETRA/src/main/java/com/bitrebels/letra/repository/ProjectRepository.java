package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bitrebels.letra.model.Project;

public interface ProjectRepository extends JpaRepository< Project, Long> {
	
}
