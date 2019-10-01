package com.bitrebels.letra.services.ApplyLeaveService;

import com.bitrebels.letra.message.request.LeaveForm;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.repository.EmployeeRepository;
import com.bitrebels.letra.repository.LeaveRequestRepository;
import com.bitrebels.letra.repository.ProgressRepo;
import com.bitrebels.letra.repository.UserRepository;
import com.bitrebels.letra.services.FireBase.NotificationService;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.LeaveHandler.ACNTypeLeaves;
import com.bitrebels.letra.services.LeaveHandler.LeaveTracker;
import com.bitrebels.letra.services.UserService;
import org.hibernate.annotations.AttributeAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ApplyLeave {

    @Autowired
    TopicService topicService;

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

    public void applyLeave(LeaveForm leaveForm , LeaveRequest leaveRequest, Set<Task> tasks ){

        Progress progress;
        Long rmID = null;
        List<Long> progressId = new ArrayList<>();

        String leaveType = leaveForm.getLeaveType();

        long leaveReqId = leaveRequest.getLeaveReqId();

        //working days between leave start date and leave end date
        int workingDays = leaveTracker.countWorkingDays(leaveForm.getSetDate(),leaveForm.getFinishDate());

        Long employeeId = userService.authenticatedUser();
        Employee employee = employeeRepository.findById(employeeId).get();

        User user = userRepo.findById(employeeId).get();

        if(user.getDeviceToken() == null){
            user.setDeviceToken(leaveForm.getDeviceToken());
        }

        String deviceToken = user.getDeviceToken();

        if((!(leaveType.equalsIgnoreCase("maternity"))) && !(Objects.isNull(employee))){

            for (Task task: tasks) {
                //requiredOrRemainingWork() method can be used either to calculate required work or remaining work

                rmID = task.getProject().getRm().getRmId();


                if(task.getEndDate().isBefore(leaveRequest.getSetDate()) || task.getStartDate().isAfter(leaveRequest.getFinishDate())){
                    continue;
                }
                ReportingManager manager = task.getProject().getRm();

                if(leaveType.equalsIgnoreCase("annual") || leaveType.equalsIgnoreCase("casual")
                        || leaveType.equalsIgnoreCase("nopay")) {

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
                progressRepo.save(progress);

                //notification received by RM

                progressId.add(progress.getProgressId());


            }

            String subsTopic = "topicRM-"+ rmID + "-EMP-" +employeeId;
            topicService.subscribe(deviceToken,subsTopic,user);

            String sendingTopic = "EmpTopic-" + employee.getEmployeeId() + "-RM-"+ rmID ;
            Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now() ,
                        leaveReqId , progressId);
            notificationService.sendToEmployeesTopic(notification);

            leaveReqRepo.save(leaveRequest);
        }
    }

    public void applyLeaveForMaternity(LeaveForm leaveForm , LeaveRequest leaveRequest){

        Progress progress;
        List<Long> progressId = new ArrayList<>();

        Long employeeId = userService.authenticatedUser();

        User user = userRepo.findById(employeeId).get();

        long leaveReqId = leaveRequest.getLeaveReqId();

        if(user.getDeviceToken() == null){
            user.setDeviceToken(leaveForm.getDeviceToken());
        }

        String deviceToken = user.getDeviceToken();

        //subscribing user to HRM's topic
        String subsTopic = "topicHRM"+ user.getHrManager().getHrmId() + "EMP" +employeeId;
        topicService.subscribe(deviceToken,subsTopic,user);

        System.out.println("HI");

        progress = new Progress();
        HRManager hrManager = user.getHrManager();
        progress.setHrManager(hrManager);
        hrManager.getProgressSet().add(progress);
        progress.setLeaveRequest(leaveRequest);
        leaveRequest.getProgressSet().add(progress);//Here it is a progress SET because 1 leave can have at most two progresses(i.e. employee working in two projects)
        progressRepo.save(progress);
        leaveReqRepo.save(leaveRequest);

        progressId.add(progress.getProgressId());


        //notification received by HRM
        String sendingTopic = "UserTopic-" + user.getId() + "-HRM-"+ user.getHrManager().getHrmId();
        Notification notification = new Notification(sendingTopic , user.getName() , LocalDate.now(),
                leaveReqId, progressId);
        notificationService.sendToEmployeesTopic(notification);
    }
}
