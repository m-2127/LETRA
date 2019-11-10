package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.*;
import com.bitrebels.letra.message.response.*;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.Date.DateToLocalDate;
import com.bitrebels.letra.services.Date.FindDatesBetween;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.HolidayReturn;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.LeaveQuota.HRMLeaveReport;
import com.bitrebels.letra.services.LeaveResponse.LeaveResponseService;
import com.bitrebels.letra.services.LeaveResponse.LeaveQuotaCal;
import com.bitrebels.letra.services.LeaveResponse.UpdateQuota;
import com.bitrebels.letra.services.ResetPassword;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class HRMRestAPI {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ACNTypeLeaves acnTypeLeaves;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	LeaveTracker leaveTracker;

	@Autowired
	HolidayReturn holidayReturn;

	@Autowired
	DateToLocalDate dateToLocalDate;

	@Autowired
	RMRepository rmRepo;

	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;

	@Autowired
	FindDatesBetween findDatesBetween;
	
	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	UserService userService;

	@Autowired
	HRMRepo hrmRepo;

	@Autowired
	LeaveQuotaCal leaveQuotaCal;

	@Autowired
	HolidayRepo holidayRepo;

	@Autowired
	ResetPassword resetPassword;

	@Autowired
	LeaveRequestRepository leaveReqRepo;

	@Autowired
    TopicService topicService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	EmployeeRepository employeeRepo;

	@Autowired
	LeaveRepo leaveRepo;

	@Autowired
	LeaveDatesRepo leaveDatesRepo;

	@Autowired
	UpdateQuota updateQuota;

	@Autowired
	NotificationService notificationService;

	@Autowired
	LeaveResponseService leaveResponseService;

	@Autowired
	HRMLeaveReport hrmLeaveReport;

	@PostMapping("/manageusers")
	@PreAuthorize("hasRole('HRM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm registrationRequest) {

		if (userRepository.existsByEmail(registrationRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}
		if (userRepository.existsByMobilenumber(registrationRequest.getMobilenumber())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Mobile number is already in use!"),
					HttpStatus.BAD_REQUEST);
		}

		long hrmId = userService.authenticatedUser();
		HRManager hrManager = hrmRepo.findById(hrmId).get();

		// Creating user's account
		User user = new User(registrationRequest.getName(), registrationRequest.getEmail(),
				encoder.encode(registrationRequest.getMobilenumber()), registrationRequest.getMobilenumber(),
				registrationRequest.getGender().toLowerCase());
		user.setHrManager(hrManager);
		Set<Role> roles = new HashSet<>();
		Role userRole1 = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole1);
		Role userRole2 = roleRepository.findByName(RoleName.ROLE_EMPLOYEE)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole2);

		user.setRoles(roles);

		user = leaveQuotaCal.updateQuotaOnRegistration(user);

		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@GetMapping("/addquota")
	@PreAuthorize("hasRole('HRM')")
	public ResponseEntity<?> addLeaveQuota() {

		long hrmId  = userService.authenticatedUser();

		System.out.println(userRepo.findById(hrmId).get().getName());

		leaveQuotaCal.updateQuotaAnnually(hrmRepo.findById(hrmId).get());

		return new ResponseEntity<>(new ResponseMessage("Leave Quota updated successfully!"), HttpStatus.OK);

	}

	@GetMapping("/setmanager")
	@PreAuthorize("hasRole('HRM')")
	public void setManager(@RequestParam Map<String, String> requestParams) {

		Long userId = Long.parseLong(requestParams.get("userId"));
		
		User user = userRepository.getOne(userId);
		
		Role userRole = roleRepository.findByName(RoleName.ROLE_RM).get();
		user.getRoles().add(userRole);

		ReportingManager manager = new ReportingManager(user.getId());//passes the user id as a parameter

		rmRepo.save(manager);
		userRepository.save(user);
	}

	@GetMapping("/findmanager")
	@PreAuthorize("hasRole('HRM')")
	public Map< Long, String > setManager() {

		 Iterator<User> iterator = userRepo.findByNonManagers().iterator();
		 Map< Long, String > userMap = new HashMap<>();

		 while(iterator.hasNext()){
		 	User currentUser = iterator.next();
		 	userMap.put(currentUser.getId(),currentUser.getName());
		 }

		 return userMap;
	}

	@PostMapping("/setholidays")
	@PreAuthorize("hasRole('HRM')")
	public  List<ApplyHoliday> setHolidays(@RequestBody HolidaySet holidaySet){
		List<ApplyHoliday> holidays = holidaySet.getHolidaySet();

		Iterator<ApplyHoliday> iterable = holidays.iterator();

		while(iterable.hasNext()){
			ApplyHoliday holiday = iterable.next();
			if(holiday.getTitle().equalsIgnoreCase("event 1"))
			{
				continue;
			}
			System.out.println(holiday.getStart());

			Holiday tempHoliday = new Holiday(dateToLocalDate.convertStringLocalDate(holiday.getStart()),
							holiday.getTitle());

			holidayRepo.save(tempHoliday);

		}

		return holidays;
//		int days = holidayRepo.countByDateBetween(LocalDate.of(2019,6,10),LocalDate.of(2019,6,13));
	}

	@GetMapping("/hrmleaveresponse")
	@PreAuthorize("hasRole('HRM')")
	public void hrmRespondToLeave(@RequestParam Map<String, String> requestParams){


		Long hrmId = userService.authenticatedUser();
		String hrmName = userRepo.findById(hrmId).get().getName();

		Long leaveReqId = Long.parseLong(requestParams.get("leaveReqId"));

		LeaveRequest leaveRequest = leaveReqRepo.findById(leaveReqId).get();

		List<LocalDate> dates = new ArrayList<>();
		dates.add(leaveRequest.getSetDate());
		dates.add(leaveRequest.getFinishDate());

		Leave leave = leaveRepo.findLeaveByLeaveRequest(leaveReqRepo.findById(leaveReqId).get());

		leave = leaveResponseService.saveLeaveDatesofHRM(dates , leave);

		Long userId = leave.getEmployee().getEmployeeId();
		User user = userRepo.findById(userId).get();

		leave.setStatus(LeaveStatus.APPROVED);
		leaveRequest.setStatus(LeaveStatus.APPROVED);
		leave.setApproval(true);

		leaveRepo.save(leave);

		updateQuota.updateMaternityQuota( findDatesBetween.getNoOfDaysBetween(leaveRequest.getSetDate(),
									leaveRequest.getFinishDate()) , user);

		leaveRepo.save(leave);
	}

	@PostMapping("/report")
	@PreAuthorize("hasRole('HRM')")
	public HRMReportDetails report(@RequestBody HRMReport hrmReport){

		long projectId = hrmReport.getProjectId();
		long employeeId = hrmReport.getEmployeeId();

		LocalDate startDate = hrmReport.getStartDate();
		LocalDate endDate = hrmReport.getFinishDate();

		Set<Leave> leaveSet = hrmLeaveReport.selectLeaves(employeeId,projectId,startDate,endDate,hrmReport);

		return hrmLeaveReport.addLeave(leaveSet);
	}

	@GetMapping("/holidays")
	@PreAuthorize("hasRole('HRM')")
	public HolidayDisplayReturn holidays(){

		return holidayReturn.returnHoliday();
	}

	@GetMapping("/findrms")
	@PreAuthorize("hasRole('HRM')")
	public Set<ReturnDetails> findRMS(){

		List<ReportingManager> reportingManagers = rmRepo.findAll();
		Iterator<ReportingManager> rmIterator  = reportingManagers.iterator();

		Set<ReturnDetails> rmDetails = new HashSet<>();

		while(rmIterator.hasNext()){
			ReportingManager manager = rmIterator.next();
			User user = userRepository.findById(manager.getRmId()).get();
			rmDetails.add(new ReturnDetails(user.getId(),user.getName()));

		}
		return rmDetails;
	}

	@GetMapping("/findemployees")
	@PreAuthorize("hasRole('HRM')")
	public Set<ReturnDetails> findEmployees(){

		List<User> users = userRepository.findAll();
		Iterator<User> userIterator  = users.iterator();

		Set<ReturnDetails> employeeDetails = new HashSet<>();

		while(userIterator.hasNext()){
			User user = userIterator.next();
			employeeDetails.add(new ReturnDetails(user.getId(),user.getName()));

		}
		return employeeDetails;
	}

	@GetMapping("/findprojects")
	@PreAuthorize("hasRole('HRM')")
	public Set<ReturnDetails> findProjects(){

		List<Project> projects = projectRepo.findAll();
		Iterator<Project> projectIterator  = projects.iterator();

		Set<ReturnDetails> projectsDetails = new HashSet<>();

		while(projectIterator.hasNext()){
			Project currentProject = projectIterator.next();
			projectsDetails.add(new ReturnDetails(currentProject.getId(),currentProject.getName()));

		}
		return projectsDetails;
	}

	@GetMapping("/homepage")
	@PreAuthorize("hasRole('HRM')")
	public Set<HRMNotificationDetails> homePages() {

		Set<LeaveRequest> leaveRequestSet = leaveReqRepo.findByLeaveTypeAndFinishDateAfter("maternity",
										LocalDate.now());

		Iterator<LeaveRequest> leavesReqIterator  = leaveRequestSet.iterator();

		Set<HRMNotificationDetails> hrmHomePageSet = new HashSet<>();

		while(leavesReqIterator.hasNext()){
			LeaveRequest leaveReq = leavesReqIterator.next();
			Leave leave = leaveRepo.findByLeaveRequest(leaveReq);
			long employeeId = leave.getEmployee().getEmployeeId();

			LocalDate leaveStart = leaveReq.getSetDate();
			LocalDate leaveEnd = leaveReq.getFinishDate();

			String name =  userRepo.findById(employeeId).get().getName();
			String leaveType = leaveReq.getLeaveType().toUpperCase();

			String leaveStatus = leave.getStatus().toString();

			if(leaveStatus.equalsIgnoreCase("approved")){
				leaveStatus = "ONGOING";
			}

			HRMNotificationDetails hrmHomePage = new HRMNotificationDetails(employeeId,leaveReq.getLeaveReqId(),
					name,leaveType,leaveStart,leaveEnd,leaveStatus);

			hrmHomePageSet.add(hrmHomePage);
		}

		return hrmHomePageSet;
	}

	@GetMapping("/projectdetails")
	@PreAuthorize("hasRole('HRM')")
	public Set<HRMProjectDetails> projectDetails() {


		List<Project> projectList =projectRepo.findAll();

		Iterator<Project> projectIterator = projectList.iterator();

		Set<HRMProjectDetails> hrmProjectDetailSet = new HashSet<>();

		while(projectIterator.hasNext()){

			int totalhours = 0 , completedhours = 0 ;double progress = 0;

			Project project = projectIterator.next();
			String projectName = project.getName();
			String managerName = userRepository.findById(project.getRm().getRmId()).get().getName();
			String status = project.getStatus().toString();

			Set<Task> taskSet = project.getTask();
			Iterator<Task> taskIterator = taskSet.iterator();

			while(taskIterator.hasNext()){
				Task task = taskIterator.next();
				totalhours += task.getHours();
				completedhours += task.getProgress();
			}
			System.out.println(totalhours);
			System.out.println(completedhours);

			double cal= (Double.valueOf(completedhours)/totalhours);


			System.out.println(cal);
			double val = round(cal,4);
			progress = val*100;


			progress = round(progress,3);

			HRMProjectDetails hrmProjectDetails = new HRMProjectDetails(projectName,managerName,progress,status);
			hrmProjectDetailSet.add(hrmProjectDetails);

		}
		return hrmProjectDetailSet;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
