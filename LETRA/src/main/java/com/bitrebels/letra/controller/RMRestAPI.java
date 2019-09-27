  package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.EmployeeAllocation;
import com.bitrebels.letra.message.request.LeaveResponse;
import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.model.leavequota.LeaveQuota;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.LeaveHandler.LeaveResponseService;
import com.bitrebels.letra.services.LeaveQuota.ManagerPDF;
import com.bitrebels.letra.services.LeaveResponse.UpdateQuota;
import com.bitrebels.letra.services.UpdateProject;
import com.bitrebels.letra.services.UpdateTask.AllocateEmployee;
import com.bitrebels.letra.services.UpdateTask.EndDateDetector;
import com.bitrebels.letra.services.UpdateTask.ProgressDetector;
import com.bitrebels.letra.services.UpdateTask.TaskStatus;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/rm")
public class RMRestAPI {

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	UpdateQuota updateQuota;
	
	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	HRMRepo hrmRepo;

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
	EndDateDetector endDateDetector;

	@Autowired
	ManagerPDF managerPDF;

	@Autowired
	ProgressDetector progressDetector;

	@Autowired
	UpdateProject updateProject;

	@Autowired
	LeaveDatesRepo leaveDatesRepo;

	@Autowired
	LeaveRepo leaveRepo;

	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;

	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskStatus taskStatus;

	@Autowired
	LeaveResponseService leaveResponseService;

	@PostMapping("/addproject")
//	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), projectForm.getFinishDate());

		Set<Task> tasks = projectForm.getTasks();

		for (Task task : tasks) {
			Timestamp timestamp = new Timestamp(new Date().getTime());
			task.setUpdateTime(timestamp);
			task.setProject(project);
			taskRepo.save(task);
		}
		project.setTask(tasks);
		project.setStatus(Status.DEVELOPMENT);

		Long userId = userService.authenticatedUser();

		ReportingManager manager = rmRepo.findById(userId).get();

		manager.setProject(project);
		project.setRm(manager);

	//	rmRepo.save(manager);
		projectRepo.save(project);


		return new ResponseEntity<>(new ResponseMessage("Project Details added successfully!"), HttpStatus.OK);
	}

	@PostMapping("/updatetask")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> updatetask(@Valid @RequestBody UpdateTask updateTask) {
		Task task;


		Long projectId = rmRepo.findById(userService.authenticatedUser()).get().getProject().getId();

        task = taskRepo.findById(updateTask.getTaskId()).get();
		Employee employee = task.getEmployee();
        task.setHours(updateTask.getDuration());
        taskRepo.save(task);

        if(Objects.isNull(task.getEmployee())){
            employee = allocateEmployee.allocateEmployee(updateTask);
            LocalDate endDate = endDateDetector.deriveEndDate(updateTask.getTaskId(),taskRepo,
                    projectId, projectRepo,employee);
            task.setEndDate(endDate);
            task.setEmployee(employee);
            taskRepo.save(task);

        }

        task = progressDetector.updateProgress(updateTask, task);

		taskStatus.updateStatus(updateTask,task);
		employeeRepo.save(employee);
		taskRepo.save(task);

		return new ResponseEntity<>(new ResponseMessage("Task Updated Successfully!"), HttpStatus.OK);
	}

	@PostMapping("/leaveresponse")
	@PreAuthorize("hasRole('RM')")
	public void respondToLeave(@Valid @RequestBody LeaveResponse leaveResponse){

		Long rmId = userService.authenticatedUser();
		ReportingManager reportingManager = rmRepo.findById(rmId).get();

		List<String> dates = leaveResponse.getDates();
		List<LeaveDates> leaveDates = new ArrayList<>();

		Leave leave = new Leave(leaveResponse.getLeaveType(), leaveResponse.getDescription(),
				dates.size(), leaveResponse.isApproval());

		leave.getReportingManager().add(reportingManager);

		leave.setLeaveDates(leaveDates);

		Long userId = leaveResponse.getEmployeeID();
		User user = userRepo.findById(userId).get();

		Employee employee = employeeRepo.findById(leaveResponse.getEmployeeID()).get();
		leave.setEmployee(employee);

		leave = leaveResponseService.saveLeaveDates(dates,leave);

		leaveRepo.save(leave);

		updateQuota.updateQuota(leaveResponse.getLeaveType(), dates.size(), user);

		//sending notification to employee who requesteed the leave
        String sendingTopic = "topicRM"+ rmId + "EMP" + userId;
        Notification notification = new Notification(sendingTopic , user.getName() , leaveResponse.isApproval());
        notificationService.sendToManagersTopic(notification);

	}

	@PostMapping("/updateProject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> updateProject(@Valid @RequestBody EmployeeAllocation employeeAllocation) {
		
		Long rmId = userService.authenticatedUser();

		ReportingManager reportingManager = rmRepo.findById(rmId).get();

		Project project = projectRepo.findByRm(reportingManager).get();

		List<Long> addedEmployees = employeeAllocation.getAddedEmp();
		List<Long> deletedEmployess = employeeAllocation.getDeletedEmp();

		updateProject.addEmployees(reportingManager, project, addedEmployees);



		return new ResponseEntity<>(new ResponseMessage("Employee  added successfully!"), HttpStatus.OK);
	}
	
	@GetMapping("/viewproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> viewproject(){

		Long userId = userService.authenticatedUser();
		
		ReportingManager rm = rmRepo.findById(userId).get();
		 
		Project project = projectRepo.findByRm(rm).get();

		return new ResponseEntity<>(new ProjectStatus(project), HttpStatus.OK);
	}

      @GetMapping("/holidayreport")
      @PreAuthorize("hasRole('RM')")
      public void holidayReport(){

//          Long userId = userService.authenticatedUser();
//
//          ReportingManager rm = rmRepo.findById(1l).get();
//          Project project = rm.getProject();
//
//          List<Employee> employeeList = employeeRepo.findByProject(project);
//		  Employee employee = employeeRepo.findById(2l).get();
//
//          managerPDF.pdfGenerator(employeeList,userRepo,rm , project);
//
//
//          //List<Leave> leave = leaveRepo.findByEmployeeAndDatesBetween(employee,LocalDate.of(2019,5,10),LocalDate.of(2019,7,13));
//          List<Leave> leave = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndReportingManager(
//          		LocalDate.of(2019,5,10), LocalDate.of(2019,7,13),employee,rm);
//          Iterator<Leave> leaveIterator = leave.iterator();
//				 while(leaveIterator.hasNext()) {
//				 	Leave leave1 = leaveIterator.next();
//			  System.out.println(leave1.getDescription() + "\n " + leave1.getLeaveType() +" \n" + leave1.getEmployee().getEmployeeId());
//		  }
	  }

	@DeleteMapping
	@PreAuthorize("hasRole('RM')")
	public void deleteProject(){

	}

	@GetMapping("/updatepage")
	@PreAuthorize("hasRole('RM')")
	public void findEmployees(){
		Set<Task> tasks = rmRepo.findById(userService.authenticatedUser()).get().getProject().getTask();

	}
}
