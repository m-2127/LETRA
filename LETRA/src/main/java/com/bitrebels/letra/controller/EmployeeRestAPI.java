package com.bitrebels.letra.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;

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

	@Autowired
	UserService userService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@PostMapping("/applyleave")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> applyLeave(@Valid @RequestBody LeaveForm leaveForm){
		
		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType(), leaveForm.getSetDate(),
				leaveForm.getFinishDate() , leaveForm.getDescription());
		
		leaveRequest.setStatus(LeaveStatus.PENDING);

		Long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();

		Set<ReportingManager> managers = employee.getManagers();

		for (ReportingManager manager : managers ) {
			leaveRequest.getReportingManagers().add(manager);//to create request_rm table
		}

		leaveReqRepo.save(leaveRequest);

        employee.getLeaveRequest().add(leaveRequest);
		employeeRepository.save(employee);

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
