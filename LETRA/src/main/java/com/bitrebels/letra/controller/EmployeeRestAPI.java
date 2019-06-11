package com.bitrebels.letra.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.LeaveStatus;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.repository.AnnualRepo;
import com.bitrebels.letra.repository.LeaveQuotaRepository;
import com.bitrebels.letra.repository.LeaveRequestRepository;
import com.bitrebels.letra.repository.UserRepository;

@RestController
@RequestMapping("/api/employee")
public class EmployeeRestAPI {
	
	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;
	
	@Autowired
	LeaveRequestRepository leaveReqRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AnnualRepo annualRepo;
	
	@PostMapping("/applyleave")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> applyLeave(@Valid @RequestBody LeaveForm leaveForm){
		
		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType(), leaveForm.getSetDate(),
				leaveForm.getFinishDate() , leaveForm.getDescription());
		
		leaveRequest.setStatus(LeaveStatus.PENDING);
		
		leaveReqRepo.save(leaveRequest);
		
		return new ResponseEntity<>(new ResponseMessage("Leave applied succesfully"), HttpStatus.OK);
		
	}
	
	@GetMapping("/leavequota")
	@PreAuthorize("hasRole('USER')")
	public List<?> viewDetails(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName()).get();
		
		
		return leaveQuotaRepo.findByUser(user);
	}

}
