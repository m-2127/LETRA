package com.bitrebels.letra.message.request;


import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bitrebels.letra.services.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class LeaveForm {
	
	@NotBlank
	private String leaveType;
	
	@NotNull
	@JsonDeserialize(using = DateHandler.class)
	private LocalDate setDate;
	
	@NotNull
	@JsonDeserialize(using = DateHandler.class)
	private LocalDate finishDate;
	
	@NotBlank
	private String description;
	

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}