package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
