  package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.*;
import com.bitrebels.letra.message.response.*;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.repository.leavequotarepo.LeaveQuotaRepository;
import com.bitrebels.letra.services.Date.FindDatesBetween;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.LeaveResponse.LeaveResponseService;
import com.bitrebels.letra.services.LeaveQuota.ManagerPDF;
import com.bitrebels.letra.services.LeaveResponse.UpdateQuota;
import com.bitrebels.letra.services.ResetPassword;
import com.bitrebels.letra.services.UpdateProject;
import com.bitrebels.letra.services.UpdateTask.*;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@RequestMapping("/api/rm")
@Configuration
@CrossOrigin
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
	ResetPassword resetPassword;

	@Autowired
	EmployeeRepository employeeRepo;

	@Autowired
	ProgressRepo progressRepo;

	@Autowired
	UserService userService;

	@Autowired
	EndDateDetector endDateDetector;

	@Autowired
	LeaveDatesRepo leaveDatesRepo;

	@Autowired
	ManagerPDF managerPDF;

	@Autowired
	ProgressDetector progressDetector;

	@Autowired
	UpdateProject updateProject;

	@Autowired
	LeaveRepo leaveRepo;

	@Autowired
	LeaveQuotaRepository leaveQuotaRepo;

	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskStatus taskStatus;

	@Autowired
	AllocateEmpToTask allocateEmpToTask;

	@Autowired
	LeaveResponseService leaveResponseService;

	@Autowired
	LeaveRequestRepository leaveRequestRepo;

	@Autowired
	FindDatesBetween findDatesBetween;

	@PostMapping("/addproject")//new project tab
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), projectForm.getFinishDate());

		Set<Task> tasks = projectForm.getTasks();

		for (Task task : tasks) {
			Timestamp timestamp = new Timestamp(new Date().getTime());
			task.setUpdateTime(timestamp);
			task.setProject(project);
			task.setStatus(Status.DEVELOPMENT);
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

	@PostMapping("/updatetask")//update project tab - edit task button
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> updatetask(@Valid @RequestBody UpdateTask updateTask) {

		Task task;

		Long projectId = rmRepo.findById(userService.authenticatedUser()).get().getProject().getId();

        task = taskRepo.findById(updateTask.getTaskId()).get();
		//Employee employee = task.getEmployee();
        task.setHours(updateTask.getDuration());
        taskRepo.save(task);

        if(Objects.isNull(task.getEmployee())){
            Employee employee = allocateEmployee.allocateEmployee(updateTask);//allocates emp to project
            LocalDate endDate = endDateDetector.deriveEndDate(updateTask.getTaskId(),
                    projectId, employee);
            allocateEmpToTask.allocateEmpToTask(task,employee);//exactly allocates emp to task
            task.setTaskEndDate(endDate);
            task.setEmployee(employee);
			employeeRepo.save(employee);

        }

        task = progressDetector.updateProgress(updateTask, task);

		taskStatus.updateStatus(updateTask,task);
		taskRepo.save(task);

		return new ResponseEntity<>(new ResponseMessage("Task Updated Successfully!"), HttpStatus.OK);
	}

	@PostMapping("/leaveresponse")//responding to leave
	@PreAuthorize("hasRole('RM')")
	public void respondToLeave(@Valid @RequestBody LeaveResponse leaveResponse){

		Leave leave;

		Long rmId = userService.authenticatedUser();
		ReportingManager reportingManager = rmRepo.findById(rmId).get();
		String managerName = userRepo.findById(rmId).get().getName();

		//retrieving dates from frontend using leaveResponse
		List<String> dates = leaveResponse.getDates();

		leave = leaveRepo.findLeaveByLeaveRequest(leaveRequestRepo.findById(leaveResponse.getLeaveReqId()).get());

		//setting the reporting manager the Leave
		leave.getReportingManager().add(reportingManager);

		leaveRepo.save(leave);//added reporting manager to leave because I need to get the sizeof the list of reporting managers

		Long userId = leaveResponse.getEmployeeID();
		User user = userRepo.findById(userId).get();

		if(leave.getReportingManager().size() != 2 ){//this runs when the initial manager responses
			leave.setLeaveType(leaveResponse.getLeaveType());

			//description is a separate entity because one leave can have many descriptions(if two managers there can be
			// two descriptions).
			leave.getDescription().add(new Description(leaveResponse.getDescription(),managerName));
			leave.setApproval(leaveResponse.isApproval());

			if(leaveResponse.isApproval()) {//this condtion is when rm approves the leave
				leaveResponseService.saveLeaveDates(dates,leave);//setting the dates which rm selected

				//even the leave is approved it is set to pending because for the leave to be approved the other RM
				// should also approve the leave
				leave.setStatus(LeaveStatus.PENDING);
				leave.setDuration(leave.getLeaveDates().size());
			}
			else{
				leave.setStatus(LeaveStatus.REJECTED);
				//sending notification to employee who requested the leave
//				String sendingTopic = "topicRM-"+ rmId + "-EMP-" + userId;
//				Notification notification = new Notification(sendingTopic , rmId+"" , leaveResponse.isApproval() , leave.getId());
//				notificationService.sendToManagersTopic(notification);
			}

			if(leave.getNoOfManagers() == 1  && leave.getStatus()==LeaveStatus.PENDING){//pending=approved
				//sending notification to employee who requested the leave
//				String sendingTopic = "topicRM-"+ rmId + "-EMP-" + userId;
//				Notification notification = new Notification(sendingTopic , rmId+"" , leaveResponse.isApproval() , leave.getId());
//				notificationService.sendToManagersTopic(notification);

				updateQuota.updateQuota(leaveResponse.getLeaveType(), leave.getLeaveDates().size() , user);
			}
		}
		else{//this works only for the second manager
			if(leave.getStatus() == LeaveStatus.PENDING){//here it checks if the previous manager has approved
				leave.getDescription().add(new Description(leaveResponse.getDescription() , managerName));
				leave.setApproval(leaveResponse.isApproval());

				if(leaveResponse.isApproval()) {//checks if the second manger approved
					leave.setStatus(LeaveStatus.APPROVED);
					leaveResponseService.updateDatesWithCurrentResponse(leave.getLeaveDates() , dates , leave);
					leave.setDuration(leave.getLeaveDates().size());
					updateQuota.updateQuota(leaveResponse.getLeaveType(), leave.getLeaveDates().size() , user);
				}
				else{
					leave.setStatus(LeaveStatus.REJECTED);
				}
				//sending notification to employee who requested the leave
//				String sendingTopic = "topicRM-"+ rmId + "-EMP-" + userId;
//				Notification notification = new Notification(sendingTopic , rmId+"" , leaveResponse.isApproval() , leave.getId());
//				notificationService.sendToManagersTopic(notification);
			}
		}

		leaveRepo.save(leave);

	}

	@PostMapping("/updateProject")//update project tab - edit project details button
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> updateProject(@Valid @RequestBody EmployeeAllocation employeeAllocation) {
		
		Long rmId = userService.authenticatedUser();
		ReportingManager reportingManager = rmRepo.findById(rmId).get();
		Project project = projectRepo.findByRm(reportingManager).get();

		List<Long> addedEmployees = employeeAllocation.getAddedEmp();
	//	List<Long> deletedEmployess = employeeAllocation.getDeletedEmp();

		updateProject.addEmployees(reportingManager, project, addedEmployees);
		updateProject.updateProjectStatusAndTasks(employeeAllocation.getStatus(),project);

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

	@GetMapping("/returnemployees1")//returns all employees of the project
	@PreAuthorize("hasRole('RM')")
	public Set<ReturnDetails> findEmployees1(){

		List<User> userList = userRepo.findAll();

		Set<ReturnDetails> returnDetailsSet = new HashSet<>();

		Iterator<User> userIterator = userList.iterator();
		while(userIterator.hasNext()){
			User user = userIterator.next();
			long id = user.getId();
			String name = user.getName();

			ReturnDetails returnDetails = new ReturnDetails(id,name);
			returnDetailsSet.add(returnDetails);
		}
		return returnDetailsSet;
	}

	@GetMapping("/returnemployees2")//returns employees under a given manager
	@PreAuthorize("hasRole('RM')")
	public Set<ReturnDetails> findEmployees2(){
		ReportingManager manager = rmRepo.findById(userService.authenticatedUser()).get();
		Set<Employee> employeeList = manager.getEmployees();

		Set<ReturnDetails> returnDetailsSet = new HashSet<>();

		Iterator<Employee> employeeIterator = employeeList.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			long id = employee.getEmployeeId();
			String name = userRepo.findById(id).get().getName();

			ReturnDetails returnDetails = new ReturnDetails(id,name);
			returnDetailsSet.add(returnDetails);
		}
		return returnDetailsSet;
	}

	@GetMapping("/selectnotification")//recommendation details
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> selectnotification(@RequestParam Map<String, String> requestParams) {

		Long leaveReqId = Long.parseLong(requestParams.get("leaveReqId"));
		LeaveRequest leaveRequest = leaveRequestRepo.findById(leaveReqId).get();
		long employeeId = leaveRequest.getEmployee().getEmployeeId();
		String name = userRepo.findById(employeeId).get().getName();

		ReportingManager manager = rmRepo.findById(userService.authenticatedUser()).get();
		Set<Progress> progresses = progressRepo.findProgressByLeaveRequestAndManager(leaveRequest, manager);

		Set<LocalDate> localDates = findDatesBetween.getDatesBetween(leaveRequest.getSetDate(),
									leaveRequest.getFinishDate().plusDays(1));

		RMNotificationDetails response  = new RMNotificationDetails(leaveRequest , progresses , employeeId , name );
		response.setDates(localDates);

		return new ResponseEntity<>(response , HttpStatus.OK);

	}

	@PostMapping("/addnewtask")//update project tab- add new task button
	@PreAuthorize("hasRole('RM')")
	public void addNewTask(@RequestBody AddNewTask addNewTask){

		Task task = new Task(addNewTask.getName(),addNewTask.getStartdate(),addNewTask.getDescription(),addNewTask.getDuration());
		String status = addNewTask.getStatus();
		if(status.equalsIgnoreCase("development")){
			task.setStatus(Status.DEVELOPMENT);
		}
		else{
			task.setStatus(Status.MAINTENANCE);
		}

		Timestamp timestamp = new Timestamp(new Date().getTime());
		task.setUpdateTime(timestamp);

		Long userId = userService.authenticatedUser();
		ReportingManager manager = rmRepo.findById(userId).get();
		Project project = manager.getProject();
		task.setProject(project);

		project.getTask().add(task);

		projectRepo.save(project);
		taskRepo.save(task);
	}

	@PostMapping("/reset")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> setNewPassword(@Valid @RequestBody ResetForm resetform) {

		User user = userRepo.findById(userService.authenticatedUser()).get();
		String password = resetform.getPassword();

		return resetPassword.setNewPassword(password, user);
	}

	@GetMapping("/homepage")
	@PreAuthorize("hasRole('RM')")
	public Set<RMHomePage> homePages() {

		ReportingManager manager = rmRepo.findById(userService.authenticatedUser()).get();

		List<Leave> leaves = leaveRepo.findByReportingManagerAndStatus(manager,LeaveStatus.PENDING);

		Iterator<Leave> leavesIterator  = leaves.iterator();

		Set<RMHomePage> rmHomePagesSet = new HashSet<>();

		while(leavesIterator.hasNext()){
			LeaveRequest leaveReq = leavesIterator.next().getLeaveRequest();
			String name =  userRepo.findById(leaveReq.getEmployee().getEmployeeId()).get().getName();
			long leaveReqId = leaveReq.getLeaveReqId();
			String leaveType = leaveReq.getLeaveType();

			RMHomePage rmHomePage = new RMHomePage(leaveReqId,leaveType,name);
			rmHomePagesSet.add(rmHomePage);
		}

		return rmHomePagesSet;
	}

	@GetMapping("/projectdetails")
	@PreAuthorize("hasRole('RM')")
	public ReturnProjectDetails returnProjectDetails()  {

		Long rmId = userService.authenticatedUser();
		ReportingManager reportingManager = rmRepo.findById(rmId).get();
		Project project = reportingManager.getProject();

		 return new ReturnProjectDetails(project.getName(),project.getStartDate(),project.getEndDate(),
						project.getStatus().toString());
	}

	@GetMapping("/taskdetails")
	@PreAuthorize("hasRole('RM')")
	public Set<ReturnTaskDetails> returnTaskDetails()  {

		Long rmId = userService.authenticatedUser();
		ReportingManager reportingManager = rmRepo.findById(rmId).get();

		Set<Task> taskSet = reportingManager.getProject().getTask();
		Iterator<Task> taskIterator = taskSet.iterator();

		Set<ReturnTaskDetails> returnTaskSet= new HashSet<>();

		while(taskIterator.hasNext()){
			Task task = taskIterator.next();
			task.getStatus().toString();



			ReturnTaskDetails returnTaskDetails = new ReturnTaskDetails(task.getId(),task.getTaskName(),
					task.getTaskStartDate(),task.getTaskEndDate(),task.getHours(),task.getProgress(),
					task.getStatus().toString()/*,empname*/);

			if(task.getEmployee() != null) {
				long employeeId = task.getEmployee().getEmployeeId();
				String empname = userRepo.findById(employeeId).get().getName();
				returnTaskDetails.setEmployee(empname);
				returnTaskDetails.setEmployeeId(employeeId);
			}
			returnTaskSet.add(returnTaskDetails);

		}

		return returnTaskSet;
	}
}
