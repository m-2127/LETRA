package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.HolidaySet;
import com.bitrebels.letra.message.request.LeaveQuotaForm;
import com.bitrebels.letra.message.request.LeaveResponse;
import com.bitrebels.letra.message.request.RegistrationForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.model.leavequota.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.LeaveHandler.LeaveResponseService;
import com.bitrebels.letra.services.LeaveResponse.LeaveQuotaCal;
import com.bitrebels.letra.services.LeaveResponse.UpdateQuota;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class HRMRestAPI {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	RMRepository rmRepo;

	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;
	
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

	@PostMapping("/registration")
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
				encoder.encode(registrationRequest.getPassword()), registrationRequest.getMobilenumber(),
				registrationRequest.getGender());
		user.setHrManager(hrManager);
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole);

		user.setRoles(roles);

		user = leaveQuotaCal.updateQuotaOnRegistration(user);

		//subscribing HRM to user topic
        String topic = "UserTopic" + user.getId() + "HRM"+ hrmId  ;
        topicService.subscribe(registrationRequest.getDeviceToken(),topic,user);

		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@PostMapping("/addquota")
	@PreAuthorize("hasRole('HRM')")
	public ResponseEntity<?> addLeaveQuota(@Valid @RequestBody LeaveQuotaForm leaveQuota) {

		AnnualLeave annualLeave = leaveQuota.getAnnualLeave();
		CasualLeave casualLeave = leaveQuota.getCasualLeave();
		SickLeave sickLeave = leaveQuota.getSickLeave();
		MaternityLeave maternityLeave = leaveQuota.getMaternityLeave();
		NoPayLeave noPayLeave = leaveQuota.getNoPayLeave();

		leaveQuotaRepo.save(annualLeave);
		leaveQuotaRepo.save(casualLeave);
		leaveQuotaRepo.save(sickLeave);
		leaveQuotaRepo.save(maternityLeave);
		leaveQuotaRepo.save(noPayLeave);

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

	@PostMapping("/setholidays")
//	@PreAuthorize("hasRole('HRM'))")
	public void setHolidays(@RequestBody HolidaySet holidaySet){
		List<Holiday> holidays = holidaySet.getHolidays();

		Iterator<Holiday> iterable = holidays.iterator();

		while(iterable.hasNext()){
			Holiday holiday = iterable.next();
			if(holiday.equals(holidays.get(0)))
			{
				continue;
			}
			holiday = new Holiday(holiday.getDate(), holiday.getDescription());

			holidayRepo.save(holiday);

		}

//		int days = holidayRepo.countByDateBetween(LocalDate.of(2019,6,10),LocalDate.of(2019,6,13));
	}

//	@PostMapping("/leaveresponse")
////	@PreAuthorize("hasRole('RM')")
//	public void hrmRespondToLeave(@Valid @RequestBody LeaveResponse leaveResponse){
//
//		Long hrmId = userService.authenticatedUser();
//		HRManager hrManager = hrmRepo.findById(hrmId).get();
//
//		List<String> dates = leaveResponse.getDates();
//		List<LeaveDates> leaveDates = new ArrayList<>();
//
//		Leave leave = new Leave(leaveResponse.getLeaveType(), leaveResponse.getDescription(),
//				dates.size(), leaveResponse.isApproval());
//
//		leave.setHrManager(hrManager);
//
//		leave.setLeaveDates(leaveDates);
//
//		Long userId = leaveResponse.getEmployeeID();
//		User user = userRepo.findById(userId).get();
//
//		Employee employee = employeeRepo.findById(leaveResponse.getEmployeeID()).get();
//		leave.setEmployee(employee);
//
//		leave = leaveResponseService.saveLeaveDates(dates , leave);
//
//		leaveRepo.save(leave);
//
//		updateQuota.updateQuota(leaveResponse.getLeaveType(), dates.size(), user);
//
//		//sending notification to employee who requesteed the leave
//		String sendingTopic = "topicRM"+ hrmId + "EMP" + userId;
//		Notification notification = new Notification(sendingTopic , user.getName() , leaveResponse.isApproval());
//		notificationService.sendToManagersTopic(notification);
//
//	}
}
