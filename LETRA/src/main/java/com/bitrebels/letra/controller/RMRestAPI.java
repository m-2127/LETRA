  package com.bitrebels.letra.controller;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import org.hibernate.hql.spi.id.persistent.UpdateHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.EmployeeAllocation;
import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.message.request.TaskForm;
import com.bitrebels.letra.message.response.JwtResponse;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.services.UserService;

import static java.time.temporal.ChronoUnit.DAYS;

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
	
	@Autowired
	UserService userService;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@PostMapping("/addproject")
//	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		int dur;

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), projectForm.getFinishDate());

		Set<Task> tasks = projectForm.getTasks();

		for (Task task : tasks) {
			long diff = (DAYS.between(task.getStartDate(),task.getEndDate()))*24;
			task.setHours(diff);
			taskRepo.save(task);
		}
		project.setTask(tasks);

		Long userId = userService.authenticatedUser();

		ReportingManager rm = rmRepo.findById(userId).get();

		project.setRm(rm);

		Long rmId = userService.authenticatedUser();
		
		ReportingManager manager = rmRepo.getOne(rmId);
		manager.setProject(project);

	//	projectRepo.save(project);
		rmRepo.save(manager);

		return new ResponseEntity<>(new ResponseMessage("Project Details added successfully!"), HttpStatus.OK);
	}
	
	@PostMapping("/updatetask")
	@PreAuthorize("hasRole('RM')")
	public void updatetask(@RequestBody UpdateTask updateTask) {

		Long rmId = userService.authenticatedUser();
		ReportingManager rm = rmRepo.findById(rmId).get();
		Set<Task> tasks = projectRepo.findByRm(rm).get().getTask();


		for (Task task: tasks) {
			Integer taskId = updateTask.getUpdatedTask().get(task.getId());
			task.setProgress(task.getProgress()+ taskId);
			taskRepo.save(task);
		}

	}
	
	@PostMapping("/allocateemployee")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> allocateEmployee(@Valid @RequestBody EmployeeAllocation employeeAllocation) {
		
		Long rmId = userService.authenticatedUser();
		
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
		//if the user is not currently working on a project

		if (!optionalemployee.isPresent()) {
			Optional<User> optionaluser = userRepo.findById(employeeAllocation.getEmployeeId());
			if (optionaluser.isPresent()) {
				User user = optionaluser.get();
				Role userRole = roleRepo.findByName(RoleName.ROLE_EMPLOYEE).get();
				user.getRoles().add(userRole);
				Employee employee = new Employee(project, manager, task, user.getId());

				employeeRepo.save(employee);
				actualManager.getEmployees().add(employee);//adding the employee to RM

				userRepo.save(user);
			} else {
				return new ResponseEntity<>(new ResponseMessage("Invalid User."), HttpStatus.BAD_REQUEST);
			}

		}

		//if the user is currently working on a project
		else {
			if (optionalemployee.isPresent()) {
				Employee employee = optionalemployee.get();
				employee.getProject().add(actualProject);
				employee.getManagers().add(actualManager);
				employee.getTasks().add(actualTask);

				employeeRepo.save(employee);

				actualManager.getEmployees().add(employee);//adding the employee to RM

			} else {
				return new ResponseEntity<>(new ResponseMessage("Invalid User."), HttpStatus.BAD_REQUEST);
			}
	
		}

		return new ResponseEntity<>(new ResponseMessage("Employee  added successfully!"), HttpStatus.OK);
	}
	
	@GetMapping("/viewproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> viewproject(){

		Long userId = userService.authenticatedUser();
		
		ReportingManager rm = rmRepo.findById(userId).get();
		
		Project project = projectRepo.findByRm(rm).get();

	 
		return ResponseEntity.ok(new ProjectStatus(project));
	}

	@MessageMapping("/view")
	@SendTo("rmtemplate/rm")
	@PreAuthorize("hasRole('RM')")
	//public List<LeaveRequest> home(){
		public String home(){
		long userId = userService.authenticatedUser();

		ReportingManager rm = rmRepo.findById(userId).get();

		List<LeaveRequest> leaveRequest = leaveRequestRepository.findByReportingManagers(rm);

		return Long.toString(userId);
	}
}
