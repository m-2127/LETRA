package com.bitrebels.letra.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ReportingManager {
	
	@Id
	private Long rmId ;
	
	@ManyToMany
	@JoinTable(name = "rm_employee",
	joinColumns = @JoinColumn(name = "rm_id"),
	inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<Employee> employees = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name="project_id")
	private Project project;
	
	public ReportingManager() {}

	public ReportingManager(Long rmId) {
		super();
		this.rmId = rmId;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
