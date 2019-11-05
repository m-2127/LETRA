  package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.*;
import com.bitrebels.letra.message.response.ProjectStatus;
import com.bitrebels.letra.message.response.RMHomePage;
import com.bitrebels.letra.message.response.RMNotificationDetails;
import com.bitrebels.letra.message.response.ResponseMessage;
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
@CrossOrigin(origins = "http://localhost:4200")
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

		List<String> dates = leaveResponse.getDates();

		leave = leaveRepo.findLeaveByLeaveRequest(leaveRequestRepo.findById(leaveResponse.getLeaveReqId()).get());

		leave.getReportingManager().add(reportingManager);
		leaveRepo.save(leave);//added reporting manager to leave because I need to get the sizeof the list of reporting managers

		Long userId = leaveResponse.getEmployeeID();
		User user = userRepo.findById(userId).get();

		if(leave.getReportingManager().size() != 2 ){//this runs when the initial manager responses
			leave.setLeaveType(leaveResponse.getLeaveType());
			leave.getDescription().add(new Description(leaveResponse.getDescription(),managerName));
			leave.setApproval(leaveResponse.isApproval());
			if(leaveResponse.isApproval()) {
				leaveResponseService.saveLeaveDates(dates,leave);
				leave.setStatus(LeaveStatus.APPROVED);//need to change it to pending
				leave.setDuration(leave.getLeaveDates().size());
			}
			else{
				leave.setStatus(LeaveStatus.REJECTED);
				//sending notification to employee who requested the leave
//				String sendingTopic = "topicRM-"+ rmId + "-EMP-" + userId;
//				Notification notification = new Notification(sendingTopic , rmId+"" , leaveResponse.isApproval() , leave.getId());
//				notificationService.sendToManagersTopic(notification);
			}

			if(leave.getNoOfManagers() == 1){
				//sending notification to employee who requested the leave
//				String sendingTopic = "topicRM-"+ rmId + "-EMP-" + userId;
//				Notification notification = new Notification(sendingTopic , rmId+"" , leaveResponse.isApproval() , leave.getId());
//				notificationService.sendToManagersTopic(notification);

				updateQuota.updateQuota(leaveResponse.getLeaveType(), leave.getLeaveDates().size() , user);
			}
		}
		else{//this works only for the second manager
			if(leave.getStatus() == LeaveStatus.APPROVED){//here it checks if the previous manager has approved
				leave.getDescription().add(new Description(leaveResponse.getDescription() , managerName));
				leave.setApproval(leaveResponse.isApproval());

				if(leaveResponse.isApproval()) {
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
		List<Long> deletedEmployess = employeeAllocation.getDeletedEmp();

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

	@DeleteMapping
	@PreAuthorize("hasRole('RM')")
	public void deleteProject(){

	}

	@GetMapping("/returnemployees1")//returns all employees of the project
	@PreAuthorize("hasRole('RM')")
	public Map<Long , String> findEmployees1(){
		List<User> userList = userRepo.findAll();
		Map<Long , String> userMap = new HashMap<>();
		Iterator<User> userIterator = userList.iterator();
		while(userIterator.hasNext()){
			User user = userIterator.next();
			long id = user.getId();
			String name = user.getName();
			userMap.put(id,name);
		}
		return userMap;
	}

	@GetMapping("/returnemployees2")//returns employees under a given manager
	@PreAuthorize("hasRole('RM')")
	public Map<Long , String> findEmployees2(){
		ReportingManager manager = rmRepo.findById(userService.authenticatedUser()).get();
		Set<Employee> employeeList = manager.getEmployees();
		Map<Long , String> employeeMap = new HashMap<>();
		Iterator<Employee> employeeIterator = employeeList.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			long id = employee.getEmployeeId();
			String name = userRepo.findById(id).get().getName();
			employeeMap.put(id,name);
		}
		return employeeMap;
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
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> setNewPassword(@Valid @RequestBody ResetForm resetform) {

		User user = userRepo.findById(userService.authenticatedUser()).get();
		String password = resetform.getPassword();

		resetPassword.setNewPassword(password, user);

		return new ResponseEntity<>(new ResponseMessage("Succesfull."), HttpStatus.BAD_REQUEST);
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

	@GetMapping("/messaging")
	@PreAuthorize("hasRole('RM')")
	public void messaging()  {

	}
}
