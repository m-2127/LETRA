package com.bitrebels.letra.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

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

	@ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name = "request_rm",
			joinColumns = @JoinColumn(name = "leave_id"),
			inverseJoinColumns = @JoinColumn(name = "rm_id"))
	@JsonIgnore
	private Set<ReportingManager> reportingManagers = new HashSet<>();

	public LeaveRequest() {
		super();
	}

	
	
	public LeaveRequest(String leaveType, LocalDate setDate, LocalDate finishDate , String description) {
		super();
		this.leaveType = leaveType;
		this.setDate = setDate;
		this.finishDate = finishDate;
		this.description = description;
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

	public Set<ReportingManager> getReportingManagers() {
		return reportingManagers;
	}

	public void setReportingManagers(Set<ReportingManager> reportingManagers) {
		this.reportingManagers = reportingManagers;
	}
}
