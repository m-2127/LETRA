package com.bitrebels.letra.services.ApplyLeaveService;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ApplyLeave {

    @Autowired
    TopicService topicService;

    @Autowired
    LeaveRepo leaveRepo;

    @Autowired
    LeaveRequestRepository leaveReqRepo;

    @Autowired
    ProgressRepo progressRepo;

    @Autowired
    UserService userService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ACNTypeLeaves acnTypeLeaves;

    @Autowired
    NotificationService notificationService;

    @Autowired
    LeaveTracker leaveTracker;

    @Autowired
    RMRepository rmRepository;

    public void applyLeave(LeaveForm leaveForm , LeaveRequest leaveRequest, Set<Task> tasks , long rmId,
                           LocalDate leaveStart, LocalDate leaveEnd){

        Progress progress = null;
        List<Long> progressId = new ArrayList<>();

        String leaveType = leaveForm.getLeaveType();

        long leaveReqId = leaveRequest.getLeaveReqId();

        ReportingManager manager = rmRepository.findById(rmId).get();

        //working days between leave start date and leave end date
        int workingDays = leaveTracker.countWorkingDays(leaveStart,leaveEnd);

        Long employeeId = userService.authenticatedUser();
        Employee employee = employeeRepository.findById(employeeId).get();

        User user = userRepo.findById(employeeId).get();

//        if(user.getDeviceToken() == null){
//            user.setDeviceToken(leaveForm.getDeviceToken());
//        }

//        String deviceToken = user.getDeviceToken();

        if((!(leaveType.equalsIgnoreCase("maternity"))) && !(Objects.isNull(employee))){

//            String subsTopic = "topicRM-"+ rmId + "-EMP-" +employeeId;
//            topicService.subscribe(deviceToken,subsTopic,user);
//
//            //notification received by RM
//            String sendingTopic = "EmpTopic-" + employee.getEmployeeId() + "-RM-"+ rmId ;
//            Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now() ,
//                    leaveReqId );

            for (Task task: tasks) {
                //requiredOrRemainingWork() method can be used either to calculate required work or remaining work

                if(task.getTaskEndDate().isBefore(leaveRequest.getSetDate()) || task.getTaskStartDate().isAfter(leaveRequest.getFinishDate())){
                    continue;
                }

                if(leaveType.equalsIgnoreCase("annual leave") || leaveType.equalsIgnoreCase("casual leave")
                        || leaveType.equalsIgnoreCase("nopay leave")) {

                    progress = acnTypeLeaves.calculateRecommendation(task, workingDays, leaveRequest);
                    if(progress==null){
                        continue;
                    }
                }
                else {
                    progress = new Progress();
                }

                progress.setManager(manager);
                manager.getProgressSet().add(progress);
                progress.setLeaveRequest(leaveRequest);
                leaveRequest.getProgressSet().add(progress);
                progress.setHrManager(user.getHrManager());
           //     progress.setNotification(notification);
                progressRepo.save(progress);

                progressId.add(progress.getProgressId());


            }

            if(progress == null){
                progress = new Progress();
                progress.setManager(manager);
                manager.getProgressSet().add(progress);
                progress.setLeaveRequest(leaveRequest);
                leaveRequest.getProgressSet().add(progress);
                progress.setHrManager(user.getHrManager());
     //           progress.setNotification(notification);
                progressRepo.save(progress);
            }

    //        notification.getProgress().add(progress);
    //        notificationService.sendToEmployeesTopic(notification);

            leaveReqRepo.save(leaveRequest);
        }
    }

    public void applyLeaveForMaternity(LeaveForm leaveForm , LeaveRequest leaveRequest,Leave leave){

        Progress progress;
        List<Long> progressList = new ArrayList<>();

        Long employeeId = userService.authenticatedUser();
        Employee employee = employeeRepository.findById(employeeId).get();

        leaveRequest.setEmployee(employee);
        leave.setEmployee(employee);

        leave.setStatus(LeaveStatus.PENDING);
        leaveRequest.setStatus(LeaveStatus.PENDING);

        leave.setLeaveType(leaveForm.getLeaveType().toLowerCase());

        User user = userRepo.findById(employeeId).get();



        long leaveReqId = leaveRequest.getLeaveReqId();



//        if(user.getDeviceToken() == null){
//            user.setDeviceToken(leaveForm.getDeviceToken());
//        }
//
//        String deviceToken = user.getDeviceToken();
//
//        //subscribing user to HRM's topic
//        String subsTopic = "topicHRM"+ user.getHrManager().getHrmId() + "EMP" +employeeId;
//        topicService.subscribe(deviceToken,subsTopic,user);

        progress = new Progress();
        HRManager hrManager = user.getHrManager();
        progress.setHrManager(hrManager);
        hrManager.getProgressSet().add(progress);
        progress.setLeaveRequest(leaveRequest);
        leaveRequest.getProgressSet().add(progress);//Here it is a progress SET because 1 leave can have at most two progresses(i.e. employee working in two projects)
        progressRepo.save(progress);

        leave.setLeaveRequest(leaveRequest);
        leaveRequest.setLeave(leave);

        leaveRepo.save(leave);
        leaveReqRepo.save(leaveRequest);

        progressList.add(progress.getProgressId());


        //notification received by HRM
        /*String sendingTopic = "UserTopic-" + user.getId() + "-HRM-"+ user.getHrManager().getHrmId();
        Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now(),
                leaveReqId);
        notification.getProgress().add(progress);
        notificationService.sendToEmployeesTopic(notification);*/
    }
}
