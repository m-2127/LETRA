package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByProject(Project project);
}
