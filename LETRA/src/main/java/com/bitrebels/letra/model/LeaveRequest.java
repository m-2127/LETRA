package com.bitrebels.letra.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private long leaveId;
	
	@Column(name = "leave_type")
	private String leaveType;
	
	@Column(name = "set_date")
	private LocalDate setDate;
	
	@Column(name = "finish_date")
	private LocalDate finishDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private LeaveStatus status;
	
	@Column(name="description")
	private String description;

	// number of the days of the leave
	private int noOfDays;

	//time at which leave was applied
	private LocalDateTime time;

	@OneToMany(mappedBy = "leaveRequest", cascade = CascadeType.ALL )
	private Set<Progress> progressSet = new HashSet<>();

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public LeaveRequest() {
		super();
	}

	
	
	public LeaveRequest(String leaveType, LocalDate setDate, LocalDate finishDate , String description,
						int noOfDays) {
		super();
		this.leaveType = leaveType;
		this.setDate = setDate;
		this.finishDate = finishDate;
		this.description = description;
		this.noOfDays = noOfDays;
	}


	public long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(long leaveId) {
		this.leaveId = leaveId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getSetDate() {
		return setDate;
	}

	public void setSetDate(LocalDate setDate) {
		this.setDate = setDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDate finishDate) {
		this.finishDate = finishDate;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}


	public Set<Progress> getProgressSet() {
		return progressSet;
	}

	public void setProgressSet(Set<Progress> progressSet) {
		this.progressSet = progressSet;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
