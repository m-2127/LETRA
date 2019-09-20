package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.response.LeaveValidation;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.AnnualRepo;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class EmployeeRestAPI {
	
	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;
	
	@Autowired
	LeaveRequestRepository leaveReqRepo;

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

	@Autowired
    TopicService topicService;

	@Autowired
    NotificationService notificationService;

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
				String subsTopic = "topicRM"+ rmID + "EMP" +employeeId;
				topicService.subscribe(deviceToken,subsTopic,user);

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

                //notification received by RM
                    String sendingTopic = "EmpTopic" + employee.getEmployeeId() + "RM"+ rmID ;
                    Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now());
                    notificationService.sendToEmployeesTopic(notification);
			}
		}
		else{

		    //subscribing user to HRM's topic
            String subsTopic = "topicHRM"+ user.getHrManager().getHrmId() + "EMP" +employeeId;
            topicService.subscribe(deviceToken,subsTopic,user);

            progress = new Progress();
            HRManager hrManager = userRepo.findById(employee.getEmployeeId()).get().getHrManager();
            progress.setHrManager(hrManager);
            progress.setLeaveRequest(leaveRequest);
            progressRepo.save(progress);
            leaveRequest.getProgressSet().add(progress);//Here it is a progress SET because 1 leave can have at most two progresses(i.e. employee working in two projects)
            leaveReqRepo.save(leaveRequest);

            //notification received by HRM
            String sendingTopic = "UserTopic" + user.getId() + "HRM"+ user.getHrManager().getHrmId();
            Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now());
            notificationService.sendToEmployeesTopic(notification);
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

//	@GetMapping("/holidayreport")
//	@PreAuthorize("hasRole('RM')")
//	public void holidayReport(){
//
//
//	}

	@GetMapping("/leavevalidation")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> leaveValidator(){

		long employeeId = userService.authenticatedUser();
		User user = userRepo.findById(employeeId).get();
		Set<LeaveQuota> leaveQuotas = user.getLeaveQuotas();
		Iterator<LeaveQuota> leaveQuotaIterator = leaveQuotas.iterator();

		int annual = 0,casual = 0, sick =0;


		while(leaveQuotaIterator.hasNext()){
			LeaveQuota currentQuota = leaveQuotaIterator.next();
			if(currentQuota instanceof AnnualLeave){
				annual = ((AnnualLeave) currentQuota).getRemainingLeaves();
			}
			if(currentQuota instanceof CasualLeave){
				casual = ((CasualLeave) currentQuota).getRemainingLeaves();
			}
			if(currentQuota instanceof SickLeave){
				sick = ((SickLeave) currentQuota).getRemainingLeaves();
			}
		}

		return new ResponseEntity<>(new LeaveValidation(annual,casual,sick, user.getGender()),HttpStatus.OK);
	}

}
