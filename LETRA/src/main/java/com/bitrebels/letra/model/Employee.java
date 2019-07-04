package com.bitrebels.letra.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Employee{
	
	@Id
	private long employeeId;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "employee_project",
	joinColumns = @JoinColumn(name = "employee_id"),
	inverseJoinColumns = @JoinColumn(name = "project_id")
	)
	private Set<Project> project = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "employee_rm",
	joinColumns = @JoinColumn(name = "employee_id"),
	inverseJoinColumns = @JoinColumn(name = "rm_id")
	)
	private Set<ReportingManager> managers = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "employee_tasks",
	joinColumns = @JoinColumn(name = "employee_id"),
	inverseJoinColumns = @JoinColumn(name = "task_id")
	)
	private Set<Task> tasks = new HashSet<>();
	
    @OneToMany
    @JoinColumn(name="employee_id")
    private Set<LeaveRequest> leaveRequest;
    
    @OneToMany
    @JoinColumn(name="employee_id")
    private Set<Leave> leave;

	public Employee() {}
	
	public Employee(Set<Project> project, Set<ReportingManager> managers, Set<Task> tasks, Long employeeId ) {
		super();
		this.project = project;
		this.managers = managers;
		this.tasks = tasks;
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
