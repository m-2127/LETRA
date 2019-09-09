package com.bitrebels.letra.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee{
	
	@Id
	private long employeeId;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "employee_project",
	joinColumns = @JoinColumn(name = "employee_id"),
	inverseJoinColumns = @JoinColumn(name = "project_id")
	)
	private Set<Project> project;
	
	@ManyToMany(mappedBy = "employees")
	private Set<ReportingManager> managers = new HashSet<>();
	
	@OneToMany(mappedBy = "employee", orphanRemoval = true)
	private Set<Task> tasks;
	
    @OneToMany
    @JoinColumn(name="employee_id")
    private Set<LeaveRequest> leaveRequest;
    
    @OneToMany(mappedBy = "employee")
    private Set<Leave> leave;

	public Employee() {}
	
	public Employee(Set<Project> project, Set<ReportingManager> managers, Long employeeId ) {
		super();
		this.project = project;
		this.managers = managers;
		this.employeeId=employeeId;
	}


	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public Set<LeaveRequest> getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(Set<LeaveRequest> leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

	public Set<Leave> getLeave() {
		return leave;
	}

	public void setLeave(Set<Leave> leave) {
		this.leave = leave;
	}

	public Set<Project> getProject() {
		return project;
	}

	public void setProject(Set<Project> project) {
		this.project = project;
	}

	public Set<ReportingManager> getManagers() {
		return managers;
	}

	public void setManagers(Set<ReportingManager> managers) {
		this.managers = managers;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}
