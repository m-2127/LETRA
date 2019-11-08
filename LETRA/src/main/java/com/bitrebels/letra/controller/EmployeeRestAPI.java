package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.HRMReport;
import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.message.request.ResetForm;
import com.bitrebels.letra.message.response.*;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.AnnualRepo;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.ApplyLeaveService.ApplyLeave;
import com.bitrebels.letra.services.Date.DateToLocalDate;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.HolidayReturn;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.ResetPassword;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/emp")
@CrossOrigin
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
	HolidayReturn holidayReturn;

	@Autowired
	RMRepository rmRepository;

	@Autowired
	UserService userService;

	@Autowired
	TaskRepository taskRepo;

	@Autowired
	ProgressRepo progressRepo;

	@Autowired
	ResetPassword resetPassword;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	LeaveTracker leaveTracker;

	@Autowired
    TopicService topicService;

	@Autowired
    NotificationService notificationService;

	@Autowired
	LeaveRepo leaveRepo;

	@Autowired
	ApplyLeave applyLeave;

	@Autowired
	DateToLocalDate dateToLocalDate;

	@PostMapping("/applyleave")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> applyLeave(@Valid @RequestBody LeaveForm leaveForm){

		LocalDate leaveStart = dateToLocalDate.convertStringLocalDate(leaveForm.getSetDate().substring(0,10));
		LocalDate leaveEnd = dateToLocalDate.convertStringLocalDate(leaveForm.getFinishDate().substring(0,10));



		//working days between leave start date and leave end date
		int duration = leaveTracker.countWorkingDays(leaveStart,leaveEnd);

		LeaveRequest leaveRequest = new LeaveRequest(leaveForm.getLeaveType().toLowerCase(), leaveStart,
				leaveEnd, leaveForm.getDescription(), duration);

		leaveReqRepo.save(leaveRequest);

		//retrieving the currently authenticated user
		Long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();

		//setting the device token to user and subscribing to the topic of current manager
		User user = userRepo.findById(employeeId).get();

		Leave leave = new Leave( LeaveStatus.PENDING , employee , user.getHrManager() , leaveRequest,
				employee.getProject().size());
		leaveRequest.setLeave(leave);
		leaveRepo.save(leave);

		leaveRequest.setTime(LocalDateTime.now());
		leaveRequest.setStatus(LeaveStatus.PENDING);
		leaveRequest.setEmployee(employee);

        employee.getLeaveRequest().add(leaveRequest);

		employeeRepository.save(employee);

		Iterator<Project> projectIterator  = employee.getProject().iterator();

		if(leaveForm.getLeaveType().equalsIgnoreCase("maternity") ){
			applyLeave.applyLeaveForMaternity(leaveForm, leaveRequest);
		}
		else {
			while (projectIterator.hasNext()) {
				Project project = projectIterator.next();
				Long rmId  = project.getRm().getRmId();
				leave.getReportingManager().add(rmRepository.findById(rmId).get());
				Set<Task> tasks = taskRepo.findTaskByEmployeeAndProject(employee, project);
				applyLeave.applyLeave(leaveForm, leaveRequest, tasks , rmId,leaveStart,leaveEnd );
			}
		}

		return new ResponseEntity<>(new ResponseMessage("Leave Applied Successfully"), HttpStatus.OK);
	}
	
	@GetMapping("/leavequota")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> viewDetails(){

		long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();
		List<LeaveQuota> leaveQuotas = leaveQuotaRepo.findByUser(userRepo.findById(employeeId).get());
		Iterator<LeaveQuota> leaveQuotaIterator = leaveQuotas.iterator();

		while(leaveQuotaIterator.hasNext()){

			LeaveQuota leaveQuota = leaveQuotaIterator.next();
			if(leaveQuota instanceof AnnualLeave){
				leaveQuota.setId(1l);
			}
			else if(leaveQuota instanceof CasualLeave){
				leaveQuota.setId(2l);
			}
			else if(leaveQuota instanceof MaternityLeave){
				leaveQuota.setId(3l);
			}
			else if(leaveQuota instanceof NoPayLeave){
				leaveQuota.setId(4l);
			}
			else{
				leaveQuota.setId(5l);
			}
		}
		return new ResponseEntity<>(new EmployeeQuotaHome(leaveQuotas), HttpStatus.OK);

	}

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

	@PostMapping("/reset")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> setNewPassword(@Valid @RequestBody ResetForm resetform) {

		User user = userRepo.findById(userService.authenticatedUser()).get();
		String password = resetform.getPassword();

		return resetPassword.setNewPassword(password, user);
	}

	@GetMapping("/selectnotification")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> requestedLeaveStatus(@RequestParam Map<String, String> requestParams) {

		Long leaveId = Long.parseLong(requestParams.get("leaveId"));
		Long rmId = Long.parseLong(requestParams.get("rmId"));
		String rmName = userRepo.findById(rmId).get().getName();
		Leave leave = leaveRepo.findById(leaveId).get();

		return new ResponseEntity<>(new EmpNotificationDetails(leave , rmName), HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/leavehistory")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public ResponseEntity<?> leaveHistory(){

		long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();
		Set<Leave> leaveSet = employee.getLeave();

		Iterator<Leave> leaveIterator = leaveSet.iterator();

		List<LeaveHistory> leaveHistories = new ArrayList<>();

		while(leaveIterator.hasNext()){
			Leave temp = leaveIterator.next();
			Set<Description> descriptions = temp.getDescription();
			Set<LeaveDates> leaveDates = temp.getLeaveDates();

			LeaveHistory leaveHistory = new LeaveHistory(temp.getId(),temp.getLeaveType(),temp.getDuration());

			leaveHistory.setDescriptions(descriptions);
			leaveHistory.setLeaveDates(leaveDates);

			leaveHistories.add(leaveHistory);
		}

		return new ResponseEntity<>(leaveHistories , HttpStatus.OK);
	}

	@GetMapping("/holidays")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public HolidayDisplayReturn holidays(){

		return holidayReturn.returnHoliday();
	}

	@GetMapping("/homepage")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public Set<EmployeeHomePage> displayhomepage(){
		long employeeId = userService.authenticatedUser();
		Employee employee = employeeRepository.findById(employeeId).get();

		Set<Leave> leaveSet = leaveRepo.findLeavesByEmployee(employee);

		Iterator<Leave> firstleaveIterator = leaveSet.iterator();
		Set<EmployeeHomePage> empHomePageSet = new HashSet<>();

		while(firstleaveIterator.hasNext()){
			Leave currentLeave = firstleaveIterator.next();
			LocalDate today = LocalDate.now();
			LocalDate endDate = currentLeave.getLeaveRequest().getFinishDate();
			LeaveRequest leaveRequest = currentLeave.getLeaveRequest();

			if(endDate.isBefore(today)){
				firstleaveIterator.remove();
			}
			else{
				EmployeeHomePage employeeHomePage  = new EmployeeHomePage(leaveRequest.getSetDate(),
						leaveRequest.getFinishDate(),leaveRequest.getNoOfDays(),
						leaveRequest.getLeaveType().toUpperCase(), currentLeave.getStatus().toString().toUpperCase());

				empHomePageSet.add(employeeHomePage);
			}
		}

//	//	Iterator<Leave> secondleaveIterator = leaveSet.iterator();
//
//		while(firstleaveIterator.hasNext()){
//
////			Leave leave = firstleaveIterator.next();
//			long leaveReqId = leave.getLeaveRequest().getLeaveReqId();
//			LeaveRequest leaveRequest = leaveReqRepo.findById(leaveReqId).get();
//
//
//
//		}

		return empHomePageSet;

	}

	@GetMapping("/report")
	@PreAuthorize("hasRole('HRM')")
	public HRMReportDetails report(){

		long employeeId = userService.authenticatedUser();

		Set<LeaveQuota> leaveQuotas = userRepo.findById(employeeId).get().getLeaveQuotas();
		Iterator<LeaveQuota> leaveQuotaIterator = leaveQuotas.iterator();

		HRMReportDetails hrmReportDetails = new HRMReportDetails();

		while(leaveQuotaIterator.hasNext()){
			LeaveQuota leaveQuota = leaveQuotaIterator.next();
			if(leaveQuota instanceof AnnualLeave){
				AnnualLeave annualLeave = (AnnualLeave) leaveQuota;
				hrmReportDetails.setAnnual(annualLeave.getRemainingLeaves());
			}
			else if(leaveQuota instanceof CasualLeave){
				CasualLeave casualLeave = (CasualLeave) leaveQuota;
				hrmReportDetails.setCasual(casualLeave.getRemainingLeaves());
			}
			else if(leaveQuota instanceof NoPayLeave){
				NoPayLeave noPayLeave = (NoPayLeave) leaveQuota;
				hrmReportDetails.setNopay(1);
			}
			else{
				SickLeave sickLeave = (SickLeave) leaveQuota;
				hrmReportDetails.setSick(sickLeave.getRemainingLeaves());
			}
		}

		return hrmReportDetails;
	}
}
