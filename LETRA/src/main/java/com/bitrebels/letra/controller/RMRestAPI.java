  package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.EmployeeAllocation;
import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/api/rm")
@RestController
public class RMRestAPI {

	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	TaskRepository taskRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RMRepository rmRepo;

	@Autowired
	EmployeeRepository employeeRepo;

	@PostMapping("/addproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), projectForm.getFinishDate());

		Set<Task> tasks = projectForm.getTasks();

		for (Task task : tasks) {
			taskRepo.save(task);
		}
		project.setTask(tasks);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Optional<User> optional = userRepo.findByEmail(auth.getName());

		Long rmId = optional.get().getId();
		ReportingManager manager = rmRepo.getOne(rmId);
		manager.setProject(project);
		project.setRm(manager);

		projectRepo.save(project);
		rmRepo.save(manager);

		return new ResponseEntity<>(new ResponseMessage("Project Details added successfully!"), HttpStatus.OK);
	}
	
	@PostMapping("/updatetask")
	@PreAuthorize("hasRole('RM')")
	public void updatetask() {
		
	}
	
	@PostMapping("/allocateemployee")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> allocateEmployee(@Valid @RequestBody EmployeeAllocation employeeAllocation) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Long rmId = userRepo.findByEmail(auth.getName()).get().getId();

		Project actualProject = projectRepo.getOne(employeeAllocation.getProjectId());

		Set<Project> project = new HashSet<Project>();
		project.add(actualProject);
		
		Task actualTask = taskRepo.findById(employeeAllocation.getTaskId()).get();
		Set<Task> task = new HashSet<Task>();
		task.add(actualTask);

		ReportingManager actualManager = rmRepo.getOne(rmId);
		Set<ReportingManager> manager = new HashSet<>();
		manager.add(actualManager);

		Optional<Employee> optionalemployee = employeeRepo.findById(employeeAllocation.getEmployeeId());

		if (!optionalemployee.isPresent()) {
			Optional<User> optionaluser = userRepo.findById(employeeAllocation.getEmployeeId());
			if (optionaluser.isPresent()) {
				User user = optionaluser.get();
				Role userRole = roleRepo.findByName(RoleName.ROLE_EMPLOYEE).get();
				user.getRoles().add(userRole);
				Employee employee = new Employee(project, manager, task, user.getId());

				employeeRepo.save(employee);
				userRepo.save(user);
			} else {
				return new ResponseEntity<>(new ResponseMessage("Invalid User."), HttpStatus.BAD_REQUEST);
			}
		} 
		else {
			if (optionalemployee.isPresent()) {
				Employee employee = optionalemployee.get();
				employee.getProject().add(actualProject);
				employee.getManagers().add(actualManager);
				employee.getTasks().add(actualTask);

				employeeRepo.save(employee);
			} else {
				return new ResponseEntity<>(new ResponseMessage("Invalid User."), HttpStatus.BAD_REQUEST);
			}
	
		}

		return new ResponseEntity<>(new ResponseMessage("Employee  added successfully!"), HttpStatus.OK);
	}
	
	@GetMapping("/viewproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> viewproject(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Long userId = userRepo.findByEmail(auth.getName()).get().getId();
		ReportingManager rm = rmRepo.findById(userId).get();
		
		Project project = projectRepo.findByRm(rm).get();

	 
		return ResponseEntity.ok(new ProjectStatus(project));
	} 
}
