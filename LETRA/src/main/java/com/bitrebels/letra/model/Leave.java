package com.bitrebels.letra.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

public class Leave {
	
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "finish_date")
	private LocalDate finishDate;
	
	@Id
	@OneToOne
	@Column(name="request_id")
	private LeaveRequest leaveRequest;
	
	private int duration;
	
	@Column(name="responded_by")
	private Set<User> responders;
	
	@Column(name="employee_id")
	@ManyToOne
	private Employee employee;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDate finishDate) {
		this.finishDate = finishDate;
	}

	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Set<User> getResponders() {
		return responders;
	}

	public void setResponders(Set<User> responders) {
		this.responders = responders;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	
}
