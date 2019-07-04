package com.bitrebels.letra.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.LeaveQuotaForm;
import com.bitrebels.letra.message.request.RegistrationForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.ReportingManager;
import com.bitrebels.letra.model.Role;
import com.bitrebels.letra.model.RoleName;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.CasualLeave;
import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.model.leavequota.MaternityLeave;
import com.bitrebels.letra.model.leavequota.NoPayLeave;
import com.bitrebels.letra.model.leavequota.SickLeave;
import com.bitrebels.letra.repository.LeaveQuotaRepository;
import com.bitrebels.letra.repository.ProjectRepository;
import com.bitrebels.letra.repository.RMRepository;
import com.bitrebels.letra.repository.RoleRepository;
import com.bitrebels.letra.repository.UserRepository;

@RestController
@RequestMapping("/api/hrm")
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

		// Creating user's account
		User user = new User(registrationRequest.getName(), registrationRequest.getEmail(),
				encoder.encode(registrationRequest.getPassword()), registrationRequest.getMobilenumber(),
				registrationRequest.getGender());

		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@PostMapping("/addquota")
	@PreAuthorize("hasRole('HRM')")
	public ResponseEntity<?> addLeaveQuota(@Valid @RequestBody LeaveQuotaForm leaveQuota) {

		AnnualLeave annualLeave = leaveQuota.getAnnualLeave();
		CasualLeave casulaLeave = leaveQuota.getCasualLeave();
		SickLeave sickLeave = leaveQuota.getSickLeave();
		MaternityLeave maternityLeave = leaveQuota.getMaternityLeave();
		NoPayLeave noPayLeave = leaveQuota.getNoPayLeave();

		leaveQuotaRepo.save(annualLeave);
		leaveQuotaRepo.save(casulaLeave);
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
}