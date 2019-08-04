package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/employee")
public class EmployeeRestAPI {

	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;

	@Autowired
	LeaveRequestRepository leaveReqRepo;

//	@Autowired
//	LeaveQuota leaveQuota;

	@Autowired
	ACNTypeLeaves acnTypeLeaves;

	@Autowired
	UserRepository userRepo;

	@Autowired
	AnnualRepo annualRepo;

	@Autowired
	UserService userService;

	@Autowired
	ProgressRepo progressRepo;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	LeaveTracker leaveTracker;

	@PostMapping("/applyleave")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> applyLeave(@Valid @RequestBody LeaveForm leaveForm){

		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType(), leaveForm.getSetDate(),
				leaveForm.getFinishDate() , leaveForm.getDescription(), leaveForm.getNoOfDays());

		leaveRequest.setStatus(LeaveStatus.PENDING);

		//retrieving the currently authenticated user
		Long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();

		Set<ReportingManager> managers = employee.getManagers();

		for (ReportingManager manager : managers ) {
			leaveRequest.getReportingManagers().add(manager);//to create request_rm table
		}

		leaveReqRepo.save(leaveRequest);

        employee.getLeaveRequest().add(leaveRequest);
		employeeRepository.save(employee);

		String leaveType = leaveForm.getLeaveType();

		Set<Task> tasks = employee.getTasks();

		if(leaveType.equalsIgnoreCase("annual") || leaveType.equalsIgnoreCase("casual")
		|| leaveType.equalsIgnoreCase("nopay")){

//			working days between leave start date and leave end date
			int workingDays = leaveTracker.countWorkingDays(leaveForm.getSetDate(),leaveForm.getFinishDate());

			for (Task task: tasks) {
				//requiredOrRemainingWork() method can be used either to calculate required work or remaining work

				Progress progress = acnTypeLeaves.calculateRecommendation(task, workingDays,leaveRequest );
				if(progress!=null){
					progressRepo.save(progress);
					leaveRequest.getProgressSet().add(progress);}
			}

		}
		else if(leaveType.equalsIgnoreCase("maternity" ) ){

		}
		else{

		}




		return new ResponseEntity<>(new ResponseMessage("Leave applied successfully"), HttpStatus.OK);

	}

	@GetMapping("/leavequota")
	@PreAuthorize("hasRole('USER')")
	public List<?> viewDetails(){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName()).get();


		return leaveQuotaRepo.findByUser(user);
	}

}
