package com.bitrebels.letra.model;

import com.bitrebels.letra.model.Firebase.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long progressId;

    private double currentProgress;

    private double requiredProgress;

    private double remainingWork;

    private double availableHoursOfWork;

    private int availableDaysForLeave;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id")
    private ReportingManager manager;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name="leave_id")
    private LeaveRequest leaveRequest;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST ,fetch = FetchType.LAZY)
    @JoinColumn(name="hrManager_id")
    private HRManager hrManager;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private String taskName;

    public Progress() {
    }

    public Progress(double currentProgress, double requiredProgress, double remainingWork,
                    double availableHoursOfWork, int availableDaysForLeave , String taskName ) {
        this.currentProgress = currentProgress;
        this.requiredProgress = requiredProgress;
        this.remainingWork = remainingWork;
        this.availableDaysForLeave = availableDaysForLeave;
        this.availableHoursOfWork = availableHoursOfWork;
        this.taskName = taskName;
    }

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public double getCurrentProgress() {
        return Math.round(currentProgress*10)/10.0;
    }

    public void setCurrentProgress(double currentProgress) {
        this.currentProgress = currentProgress;
    }

    public double getRequiredProgress() {
        return Math.round(requiredProgress*10)/10.0;
    }

    public void setRequiredProgress(double requiredProgress) {
        this.requiredProgress = requiredProgress;
    }

    public double getRemainingWork() {
        return Math.round(remainingWork*10)/10.0;
    }

    public void setRemainingWork(double remainingWork) {
        this.remainingWork = remainingWork;
    }

    public int getAvailableDaysForLeave() {
        return availableDaysForLeave;
    }

    public void setAvailableDaysForLeave(int availableDaysForLeave) {
        this.availableDaysForLeave = availableDaysForLeave;
    }

    public double getAvailableHoursOfWork() {
        return Math.round(availableHoursOfWork*10)/10.0;
    }

    public void setAvailableHoursOfWork(double availableHoursOfWork) {
        this.availableHoursOfWork = availableHoursOfWork;
    }

    public ReportingManager getManager() {
        return manager;
    }

    public void setManager(ReportingManager manager) {
        this.manager = manager;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public HRManager getHrManager() {
        return hrManager;
    }

    public void setHrManager(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
