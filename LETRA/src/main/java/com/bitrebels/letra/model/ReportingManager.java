package com.bitrebels.letra.model;

import org.hibernate.validator.constraints.EAN;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ReportingManager {
	
	@Id
	private long rmId ;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "rm_employee",
	joinColumns = @JoinColumn(name = "rm_id"),
	inverseJoinColumns = @JoinColumn(name = "employee_id")
	)
	private Set<Employee> employees = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="project_id",referencedColumnName = "project_id")
	private Project project;

	@OneToMany(mappedBy = "manager" , cascade = CascadeType.ALL)
	private Set<Progress> progressSet;

	@ManyToMany(mappedBy = "reportingManager" , cascade = CascadeType.PERSIST)
	private List<Leave> leaveList;

	//this mapping is for the case of creating sql join statements
//	@OneToOne
//	@JoinColumn(name="user_id",referencedColumnName = "user_id")
//	private User user;
	
	public ReportingManager() {}

	public ReportingManager(long rmId) {
		super();
		this.rmId = rmId;
	}

	public long getRmId() {
		return rmId;
	}

	public void setRmId(long rmId) {
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

	public Set<Progress> getProgressSet() {
		return progressSet;
	}

	public void setProgressSet(Set<Progress> progressSet) {
		this.progressSet = progressSet;
	}

	public List<Leave> getLeaveList() {
		return leaveList;
	}

	public void setLeaveList(List<Leave> leaveList) {
		this.leaveList = leaveList;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
}
