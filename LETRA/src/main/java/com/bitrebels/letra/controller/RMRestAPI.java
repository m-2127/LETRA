  package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.EmployeeAllocation;
import com.bitrebels.letra.message.request.LeaveResponse;
import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.LeaveQuota.ManagerPDF;
import com.bitrebels.letra.services.LeaveResponse.UpdateQuota;
import com.bitrebels.letra.services.UpdateProject;
import com.bitrebels.letra.services.UpdateTask.AllocateEmployee;
import com.bitrebels.letra.services.UpdateTask.EndDateDetector;
import com.bitrebels.letra.services.UpdateTask.ProgressDetector;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

  @RestController
@RequestMapping("/api/auth")
public class RMRestAPI {

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	UpdateQuota updateQuota;
	
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

		Long userId = userService.authenticatedUser();

		ReportingManager rm = rmRepo.findById(userId).get();

		project.setRm(rm);

		Long rmId = userService.authenticatedUser();
		
		ReportingManager manager = rmRepo.getOne(rmId);
		manager.setProject(project);
		project.setRm(manager);

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

	@PostMapping("/leaveresponse")
//	@PreAuthorize("hasRole('RM')")
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

		leaveRepo.save(leave);

		for(String date : dates) {
			LocalDate localDate = LocalDate.parse(date);
			LeaveDates temp = new LeaveDates(localDate);
			leaveDates.add(temp);
			leaveDatesRepo.save(temp);
		}

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

	@MessageMapping("/view")
	@SendTo("/rmtemplate/rm")
		public Hello greeting(Email email) throws Exception{


		return new Hello("Hi " + email.getEmail());
	}

      @GetMapping("/holidayreport")
 //     @PreAuthorize("hasRole('RM')")
      public void holidayReport(){

//          Long userId = userService.authenticatedUser();
//
          ReportingManager rm = rmRepo.findById(1l).get();
          Project project = rm.getProject();

          List<Employee> employeeList = employeeRepo.findByProject(project);
		  Employee employee = employeeRepo.findById(2l).get();

          managerPDF.pdfGenerator(employeeList,userRepo,rm , project);


          //List<Leave> leave = leaveRepo.findByEmployeeAndDatesBetween(employee,LocalDate.of(2019,5,10),LocalDate.of(2019,7,13));
          List<Leave> leave = leaveRepo.findByLeaveDates_DateBetweenAndEmployeeAndReportingManager(
          		LocalDate.of(2019,5,10), LocalDate.of(2019,7,13),employee,rm);
          Iterator<Leave> leaveIterator = leave.iterator();
				 while(leaveIterator.hasNext()) {
				 	Leave leave1 = leaveIterator.next();
			  System.out.println(leave1.getDescription() + "\n " + leave1.getLeaveType() +" \n" + leave1.getEmployee().getEmployeeId());
		  }

      }
}
