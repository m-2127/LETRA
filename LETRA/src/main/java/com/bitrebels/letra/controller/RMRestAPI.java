  package com.bitrebels.letra.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.UpdateTask.AllocateEmployee;
import com.bitrebels.letra.services.UpdateTask.EndDateDetector;
import com.bitrebels.letra.services.UpdateTask.ProgressDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.model.Status;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.services.UserService;

@RequestMapping("/api/auth")
@RestController
public class RMRestAPI {

	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	AllocateEmployee allocateEmployee;

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

	@Autowired
	EndDateDetector endDateDetector;

	@Autowired
	ProgressDetector progressDetector;

	@PostMapping("/addproject")
//	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), projectForm.getFinishDate());

		Set<Task> tasks = projectForm.getTasks();

		for (Task task : tasks) {
			Timestamp timestamp = new Timestamp(new Date().getTime());
			task.setUpdateTime(timestamp);
			taskRepo.save(task);
		}
		project.setTask(tasks);
		project.setStatus(Status.DEVELOPMENT);

		Long rmId = userService.authenticatedUser();
		
		ReportingManager manager = rmRepo.getOne(rmId);
		manager.setProject(project);
		//project.setRm(manager);

	//	projectRepo.save(project);
		rmRepo.save(manager);

		return new ResponseEntity<>(new ResponseMessage("Project Details added successfully!"), HttpStatus.OK);
	}

	@PostMapping("/updatetask")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> updatetask(@ Valid @RequestBody UpdateTask updateTask) {
		Task task;

        task = taskRepo.findById(updateTask.getTaskId()).get();

        if(Objects.isNull(task.getEmployee())){
            Employee employee = allocateEmployee.allocateEmployee(updateTask);
            LocalDate endDate = endDateDetector.deriveEndDate(updateTask.getTaskId(),taskRepo,
                    updateTask.getProjectId(), projectRepo,employee);
            task.setEndDate(endDate);

        }

        task = progressDetector.updateProgress(updateTask, task);

		if(updateTask.getStatus().equalsIgnoreCase("COMPLETED")){
			task.setStatus(Status.COMPLETED);
		}
		taskRepo.save(task);

		return new ResponseEntity<>(new ResponseMessage("Task Updated Successfully!"), HttpStatus.OK);

	}
	
	@GetMapping("/viewproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> viewproject(){

		Long userId = userService.authenticatedUser();
		
		ReportingManager rm = rmRepo.findById(userId).get();
		
		Project project = projectRepo.findByRm(rm).get();

		return new ResponseEntity<>(new ProjectStatus(project), HttpStatus.OK);
	}

	@MessageMapping("/view")
	@SendTo("rmtemplate/rm")
	@PreAuthorize("hasRole('RM')")
	public List<LeaveRequest> home(){
		Long userId = userService.authenticatedUser();

		ReportingManager rm = rmRepo.findById(userId).get();

		List<LeaveRequest> leaveRequest = leaveRequestRepository.findByReportingManagers(rm);

		return leaveRequest;
	}


}
