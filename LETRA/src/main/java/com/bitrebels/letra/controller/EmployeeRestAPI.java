package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.AnnualRepo;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
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

		Progress progress;

		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType(), leaveForm.getSetDate(),
				leaveForm.getFinishDate() , leaveForm.getDescription(), leaveForm.getNoOfDays());

		leaveRequest.setStatus(LeaveStatus.PENDING);

		//retrieving the currently authenticated user
		Long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();

		//setting the device token to user and subscribing to the topic of current manager
		User user = userRepo.findById(employeeId).get();
		if(user.getDeviceToken() == null){
			user.setDeviceToken(leaveForm.getDeviceToken());
		}

		String deviceToken = user.getDeviceToken();

        employee.getLeaveRequest().add(leaveRequest);
		employeeRepository.save(employee);

		String leaveType = leaveForm.getLeaveType();

		Set<Task> tasks = employee.getTasks();

		//			working days between leave start date and leave end date
		int workingDays = leaveTracker.countWorkingDays(leaveForm.getSetDate(),leaveForm.getFinishDate());


		if(!(leaveType.equalsIgnoreCase("maternity"))){


			for (Task task: tasks) {
				//requiredOrRemainingWork() method can be used either to calculate required work or remaining work

				Long rmID = task.getProject().getRm().getRmId();
				String topic = "topicRM"+rmID;

				if(task.getEndDate().isBefore(leaveRequest.getSetDate()) || task.getStartDate().isAfter(leaveRequest.getFinishDate())){

					continue;
				}
				ReportingManager manager = task.getProject().getRm();

				if(leaveType.equalsIgnoreCase("annual") || leaveType.equalsIgnoreCase("casual")
						|| leaveType.equalsIgnoreCase("nopay")) {

					progress = acnTypeLeaves.calculateRecommendation(task, workingDays, leaveRequest);
					if(progress==null){
						continue;
					}
				}
				else {
						progress = new Progress();
				}
					progress.setManager(manager);
					progress.setLeaveRequest(leaveRequest);
					progressRepo.save(progress);//i think it is not necessary to save this because when leave request is saved, the progress is also saved automatically
					leaveRequest.getProgressSet().add(progress);
					leaveReqRepo.save(leaveRequest);
			}
		}
		else{
				progress = new Progress();
				HRManager hrManager = userRepo.findById(employee.getEmployeeId()).get().getHrManager();
				progress.setHrManager(hrManager);
				progress.setLeaveRequest(leaveRequest);
				progressRepo.save(progress);
				leaveRequest.getProgressSet().add(progress);
				leaveReqRepo.save(leaveRequest);
		}
		return new ResponseEntity<>(new ResponseMessage("Leave applied successfully"), HttpStatus.OK);
	}
	
	@GetMapping("/leavequota")
//	@PreAuthorize("hasRole('USER')")
	public List<?> viewDetails(){

		long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();
		return leaveQuotaRepo.findByUser(new User());
	}

	@GetMapping("/holidayreport")
	@PreAuthorize("hasRole('RM')")
	public void holidayReport(){


	}

}
