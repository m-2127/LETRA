package com.bitrebels.letra.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.LeaveStatus;
import com.bitrebels.letra.repository.LeaveRequestRepository;

@RestController
@RequestMapping("/api/employee")
public class EmployeeRestAPI {
	
	@Autowired
	LeaveRequestRepository leaveReqRepo;
	
	@PostMapping("/applyleave")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> applyLeave(@Valid @RequestBody LeaveForm leaveForm){
		
		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType(), leaveForm.getSetDate(),leaveForm.getFinishDate());
		
		leaveRequest.setStatus(LeaveStatus.PENDING);
		
		leaveReqRepo.save(leaveRequest);
		
		return new ResponseEntity<>(new ResponseMessage("Leave applied succesfully"), HttpStatus.OK);
		
	}

}
