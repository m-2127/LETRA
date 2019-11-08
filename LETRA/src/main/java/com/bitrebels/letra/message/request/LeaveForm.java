package com.bitrebels.letra.message.request;


import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bitrebels.letra.services.Date.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class LeaveForm {
	
	@NotBlank
	private String leaveType;
	
//	@NotNull
//	@JsonDeserialize(using = DateHandler.class)
//	private LocalDate setDate;
//
//	@NotNull
//	@JsonDeserialize(using = DateHandler.class)
//	private LocalDate finishDate;

	@NotNull
	private String setDate;

	@NotNull
	private String finishDate;
	
	@NotBlank
	private String description;

//	@NotNull
//	private int noOfDays;

//	private String deviceToken;
	

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public int getNoOfDays() {
//		return noOfDays;
//	}
//
//	public void setNoOfDays(int noOfDays) {
//		this.noOfDays = noOfDays;
//	}
//
//	public String getDeviceToken() {
//		return deviceToken;
//	}
//
//	public void setDeviceToken(String deviceToken) {
//		this.deviceToken = deviceToken;
//	}
}
